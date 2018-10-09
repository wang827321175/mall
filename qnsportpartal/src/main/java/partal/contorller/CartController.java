package partal.contorller;


import common.Constants;
import common.LocalSessionProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.cart.Cart;
import pojo.cart.CartCriteria;
import pojo.cart.UserCart;
import pojo.cart.UserItem;
import pojo.product.*;
import pojo.user.Addr;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import service.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * 购物车的controller
 *
 * @author
 */
@Controller
public class CartController {

    @Autowired
    SkuService skuService;

    @Autowired
    LocalSessionProvider localSessionProvider;

    @Autowired
    JedisPool jedisPool;

    @Autowired
    AddrService addrService;

    @Autowired
    CartService cartService;

    @Autowired
    FeatureService featureService;

    @Autowired
    ProductService productService;

    ObjectMapper om = new ObjectMapper();

    @RequestMapping("/buy/shopping.html")
    public String cart(Long skuId, Integer amount, Model model, HttpServletResponse response,
                       HttpServletRequest request) throws IOException {
        //过滤null值
        om.setSerializationInclusion(Inclusion.NON_NULL);
        //创建购物车
        UserCart userCart = null;
        Cookie[] cookies = request.getCookies();
        //在cookie中查找购物车
        userCart = findUserCartByCookies(cookies);
        //判断购物车是否为空
        if (userCart == null) {
            userCart = new UserCart();
        }
        //判断用户是否登录
        String username = localSessionProvider.getAttribute(request, response, Constants.USER_NAME);
        if (username != null) {
            //登录情况下
            Jedis jedis = jedisPool.getResource();
            //将cookie中的信息存储到redis
            List<UserItem> userItems = userCart.getItems();
            for (UserItem userItem : userItems) {
                //保存购物项,如果存在就追加
                if (jedis.hexists("userItem:" + username, skuId.toString())) {
                    jedis.hincrBy("userItem:" + username, skuId.toString(), amount);
                } else {
                    jedis.lpush("userCart:" + username, userItem.getSku().getId().toString());
                    //保存购物项中的数据
                    jedis.hset("userItem:" + username, userItem.getSku().getId().toString(), userItem.getAmount().toString());
                }
            }
            //清空购物车
            userCart.clearCart();
            clearCookie(response);
            //添加商品到redis
            if (skuId != null) {
                //如果redis中已经存在
                if (jedis.hexists("userItem:" + username, skuId.toString())) {
                    jedis.hincrBy("userItem:" + username, skuId.toString(), amount);
                } else {
                    //保存购物项
                    jedis.lpush("userCart:" + username, skuId.toString());
                    //保存购物项中的数据
                    jedis.hset("userItem:" + username, skuId.toString(), amount.toString());
                }
            }
            //把redis中的数据添加到购物车
            List<String> keys = jedis.lrange("userCart:" + username, 0, -1);
            if (keys != null) {
                for (String skId : keys) {
                    Sku sku = new Sku();
                    sku.setId(Long.parseLong(skId));

                    //创建购物项
                    UserItem userItem = new UserItem();
                    userItem.setSku(sku);
                    userItem.setAmount(Integer.parseInt(jedis.hget("userItem:" + username, skId)));

                    //添加到购物车
                    userCart.addUserItem(userItem);
                }
            }
        } else {
            //非登录状态
            //商品添加到购物车
            if (skuId != null) {
                //商品添加到购物车
                userItemToCart(userCart, skuId, amount, response);
            }
        }

        //页面购物车信息,需要加载全部数据,然后返回页面进行显示
        showCart(userCart);

        //对购物车中的商品进行排序
        Collections.sort(userCart.getItems(), new Comparator<UserItem>() {
            @Override
            public int compare(UserItem o1, UserItem o2) {
                return -1;
            }
        });


        model.addAttribute("userCart", userCart);
        return "product/cart";
    }


    /**
     * 获取购物车数据加载
     *
     * @param userCart
     */
    private void showCart(UserCart userCart) {
        List<UserItem> userItems = userCart.getItems();
        if (userItems != null) {
            for (UserItem userItem : userItems) {

                Sku sku = skuService.loadById(userItem.getSku().getId());
                userItem.setSku(sku);
            }
        }
    }

    /**
     * 添加商品到购物车回写cookie
     *
     * @param userCart
     * @param skuId
     * @param amount
     * @param response
     */
    private void userItemToCart(UserCart userCart, Long skuId, Integer amount, HttpServletResponse response) throws IOException {
        Sku sku = new Sku();
        sku.setId(skuId);

        //创建购物项
        UserItem userItem = new UserItem();
        userItem.setAmount(amount);
        userItem.setSku(sku);

        //购物项添加到购物车
        userCart.addUserItem(userItem);

        om.setSerializationInclusion(Inclusion.NON_NULL);

        StringWriter w = new StringWriter();
        //购物车转换为json串
        om.writeValue(w, userCart);

        //回写浏览器
        Cookie cookie = new Cookie(Constants.USER_CART, w.toString());
        //保存一周
        cookie.setMaxAge(60 * 60 * 24 * 7);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 删除商品后回写浏览器
     *
     * @param userCart
     * @param response
     * @throws IOException
     */
    private void userItemToCart(UserCart userCart, HttpServletResponse response) throws IOException {
        StringWriter w = new StringWriter();
        om.setSerializationInclusion(Inclusion.NON_NULL);
        om.writeValue(w, userCart);

        //回写浏览器
        Cookie cookie = new Cookie(Constants.USER_CART, w.toString());
        cookie.setMaxAge(60 * 60 * 24 * 7);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 在cookie中查询购物车
     *
     * @param cookies
     * @return
     */
    private UserCart findUserCartByCookies(Cookie[] cookies) throws IOException {
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (Constants.USER_CART.equals(cookie.getName())) {
                    String value = cookie.getValue();
                    return om.readValue(value, UserCart.class);
                }
            }
        }
        return null;
    }

    /**
     * 删除购物车中的购物项
     *
     * @param skuId
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/shopping/delProduct.html")
    public String delProduct(Long skuId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = localSessionProvider.getAttribute(request, response, Constants.USER_NAME);
        if (username != null) {
            //删除购物车好购物项
            Jedis jedis = jedisPool.getResource();
            //删除购物车
            jedis.lrem("userCart:" + username, 0, skuId.toString());
            //删除购物项
            jedis.hdel("userItem:" + username, skuId.toString());
        } else {
            om.setSerializationInclusion(Inclusion.NON_NULL);
            Cookie[] cookies = request.getCookies();
            UserCart userCart = findUserCartByCookies(cookies);

            //回写浏览器
            userCart.delUserItem(skuId);
            userItemToCart(userCart, response);
        }
        return "redirect:/buy/shopping.html";
    }


    @RequestMapping("/shopping/addAmount.html")
    public void addAmount(Long skuId, Integer amount, HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserCart userCart = new UserCart();

        //判断用户是否登录
        String username = localSessionProvider.getAttribute(request, response, Constants.USER_NAME);
        if (username != null) {
            Jedis jedis = jedisPool.getResource();
            //数量追加
            jedis.hincrBy("userItem:" + username, skuId.toString(), Long.parseLong(amount.toString()));

            //封装购物车
            List<String> keys = jedis.lrange("userCart:" + username, 0, -1);
            for (String key : keys) {
                Sku sku = new Sku();
                sku.setId(Long.parseLong(key));
                UserItem userItem = new UserItem();
                userItem.setSku(sku);
                userItem.setAmount(Integer.parseInt(jedis.hget("userItem:" + username, key.toString())));
                userCart.addUserItem(userItem);
            }
        } else {
            om.setSerializationInclusion(Inclusion.NON_NULL);

            //找出cookie中存储的信息
            userCart = findUserCartByCookies(request.getCookies());

            //追加商品
            userItemToCart(userCart, skuId, amount, response);
        }
        //回显数据
        showCart(userCart);

        //将重新计算的小计,价格,运费,总价格添加到json
        JSONObject json = new JSONObject();
        json.put("productAmount", userCart.getProductAmount());
        json.put("price", userCart.getPrice());
        json.put("extra", userCart.getExtra());
        json.put("allPrice", userCart.getAllPrice());
        response.getWriter().write(json.toString());

    }

    /**
     * 清空购物车
     *
     * @return
     */
    @RequestMapping("/shopping/clearCart.html")
    public String clearCart(HttpServletRequest request, HttpServletResponse response) {
        String username = localSessionProvider.getAttribute(request, response, Constants.USER_NAME);
        if (username != null) {
            Jedis jedis = jedisPool.getResource();
            //删除购物车
            //删除购物车
            jedis.del("userCart:" + username);
            //删除购物项
            jedis.del("userItem:" + username);

        }
        //清空cookie
        Cookie cookie = new Cookie(Constants.USER_CART, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/buy/shopping.html";
    }

    private void clearCookie(HttpServletResponse response) {
        //清空cookie
        Cookie cookie = new Cookie(Constants.USER_CART, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    /**
     * 购物车结算
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("/buy/balanceAccounts.html")
    public String balanceAccounts(HttpServletRequest request, HttpServletResponse response, Model model) {
        UserCart userCart = new UserCart();

        //结算必须登录
        String username = localSessionProvider.getAttribute(request, response, Constants.USER_NAME);
        Jedis jedis = jedisPool.getResource();

        //如果购买限制大于库存是无货,需要跳转到商品显示页
        Boolean flag = true;

        //判断购物车中是否有商品
        List<String> keys = jedis.lrange("userCart:" + username, 0, -1);
        if (keys != null && keys.size() != 0) {
            for (String key : keys) {
                Sku sku = new Sku();
                sku.setId(Long.parseLong(key));
                UserItem userItem = new UserItem();
                userItem.setSku(sku);
                userItem.setAmount(Integer.parseInt(jedis.hget("userItem:" + username, key)));
                //判断购物项是否有货
                Sku s = skuService.loadById(Long.parseLong(key));
                if (s.getStock() < userItem.getAmount()) {
                    //说明有货
                    userItem.setHave(false);
                    flag = false;
                }
                userCart.addUserItem(userItem);
            }

            //数据加载
            showCart(userCart);

            if (flag) {
                //所有商品都有货 需要通过用户名称去查询地址等信息
                List<Addr> addr = addrService.selectAddrByUsername(username);
                model.addAttribute("addr", addr);
                //跳转订单详情页面
                model.addAttribute("userCart", userCart);
                return "product/productOrder";
            } else {
                //包含无货的商品
                model.addAttribute("userCart", userCart);
                return "product/cart";
            }
        } else {
            //购物车为空
            return "redirect:/buy/shopping.html";
        }
    }

    //------------------以下是修改之后的代码----------------------

    /**
     * 添加到购物车
     *
     * @param cart
     * @param model
     * @return
     */
    @RequestMapping("/cart/addToCart.html")
    public String addToCart(Cart cart, Model model) {
        String username = "tom";
        cart.setUsername(username);
        cartService.addToCart(cart);
        model.addAttribute("id", cart.getId());
        return "redirect:/product/detail.html";
    }

    /**
     * 跳转购物车
     *
     * @param username
     * @param storeId
     * @param model
     * @return
     */
    @RequestMapping("/cart/goToCart.html")
    public String goToCart(String username, String storeId, Model model) {
        //根据用户名来显示该用户购物车
        List<Cart> carts = cartService.selectByUsername(username);
        List<Sku> skus = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        HashMap<Long, Float> pPriceTotal = new HashMap<>();
        float totalPrice = 0f;
        for (int i = 0; i < carts.size(); i++) {
            Long id = carts.get(i).getId();
            float price = skuService.selectPriceByProductId(id);
            //商品价格
            Sku sku = new Sku();
            sku.setProductId(carts.get(i).getId());
            sku.setPrice(price);
            Integer count = carts.get(i).getCount();
            totalPrice += count * price;
            skus.add(sku);

            Product product = productService.selectByProductId(id);
            //加载sku
            products.add(product);

            //购物车每个商品价格小计
            pPriceTotal.put(id, totalPrice);

        }
        model.addAttribute("pPriceTotal", pPriceTotal);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("products", products);
        model.addAttribute("skus", skus);

        model.addAttribute("carts", carts);
        if (storeId != null) {
            //带有id为跳转购物车
            model.addAttribute("storeId", storeId);
            return "/product/mobileCart";
        } else {
            return "/buyer/mobileOrder";
        }

    }

    /**
     * 购物车数量增减
     *
     * @param id
     * @param amount
     * @param response
     * @throws IOException
     */
    @RequestMapping("/cart/addAmount.html")
    public void addAmount(Long id, Integer amount, String username, HttpServletResponse response) throws IOException {
        JSONObject json = new JSONObject();
        //修改数据库中的数据
        Cart cart = cartService.selectById(id);
        cart.setCount(amount);
        cartService.updateCount(cart);

        //修改页面显示的总价
        //根据id找出价格
        float totalPrice = 0;
        List<Cart> carts = cartService.selectByUsername(username);
        for (int i = 0; i < carts.size(); i++) {
            Long cid = carts.get(i).getId();
            float price = skuService.selectPriceByProductId(cid);
            Integer count = cartService.selectById(cid).getCount();
            float pPrice = price * count;
            json.put("pPrice" + i, pPrice);
            totalPrice += pPrice;
        }


        json.put("amount", amount);
        json.put("totalPrice", totalPrice);

        response.getWriter().write(json.toString());
    }

    /**
     * 删除购物车中的商品
     *
     * @param id
     * @param username
     * @param model
     * @param response
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/cart/deleteCart.html")
    public void deleteCart(Long id, String username, Model model, HttpServletResponse response) throws IOException {
        CartCriteria cartCriteria = new CartCriteria();
        cartCriteria.createCriteria().andUsernameEqualTo(username).andIdEqualTo(id);
        cartService.deleteCart(cartCriteria);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", "success");
        response.getWriter().write(jsonObject.toString());
    }

    @RequestMapping("/cart/reCalculation.html")
    public void reCalculation(String username, HttpServletResponse response) throws IOException {
        JSONObject json = new JSONObject();
        float totalPrice = 0;
        List<Cart> carts = cartService.selectByUsername(username);
        for (int i = 0; i < carts.size(); i++) {
            Long cid = carts.get(i).getId();
            float price = skuService.selectPriceByProductId(cid);
            Integer count = cartService.selectById(cid).getCount();
            float pPrice = price * count;
            json.put("pPrice" + i, pPrice);
            totalPrice += pPrice;
        }
        json.put("totalPrice", totalPrice);

        response.getWriter().write(json.toString());
    }

    /**
     * 生成订单
     *
     * @param username
     * @return
     */
    @RequestMapping("/cart/toOrderPage.html")
    public String toOrderPage(String username, String storeId, Model model) {
        //查询地址
        List<Addr> addrList = addrService.selectAddrByUsername(username);
        model.addAttribute("addrList", addrList);

        //查询购物车
        List<Cart> carts = cartService.selectByUsername(username);
        List<Sku> skus = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        HashMap<Long, Float> pPriceTotal = new HashMap<>();
        float totalPrice = 0f;
        for (int i = 0; i < carts.size(); i++) {
            Long id = carts.get(i).getId();
            //商品单价
            float price = skuService.selectPriceByProductId(id);
            //商品价格
            Sku sku = new Sku();
            sku.setProductId(carts.get(i).getId());
            sku.setPrice(price);
            //商品数量
            Integer count = carts.get(i).getCount();
            totalPrice += count * price;
            skus.add(sku);

            Product product = productService.selectByProductId(id);
            //加载sku
            products.add(product);

            //购物车每个商品价格小计
            pPriceTotal.put(id, totalPrice);

        }
        model.addAttribute("addrList", addrList);
        model.addAttribute("pPriceTotal", pPriceTotal);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("products", products);
        model.addAttribute("skus", skus);

        model.addAttribute("carts", carts);
        model.addAttribute("storeId", storeId);

        return "buyer/mobileOrder";
    }
}
