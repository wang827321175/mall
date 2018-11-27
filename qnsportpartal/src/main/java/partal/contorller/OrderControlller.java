package partal.contorller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.Constants;
import common.LocalSessionProvider;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import pojo.cart.UserCart;
import pojo.cart.UserItem;
import pojo.order.Detail;
import pojo.order.DetailVO;
import pojo.order.Order;
import pojo.product.Sku;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import service.OrderService;
import service.SkuService;

/***
 * @author
 * 订单模块
 */

@Controller
public class OrderControlller {

    @Autowired
    LocalSessionProvider localSessionProvider;

    @Autowired
    JedisPool jedisPool;

    @Autowired
    SkuService skuService;

    @Autowired
    OrderService orderService;

    /**
     * 生成随机订单号
     *
     * @return
     */
    public static String getOrderIdByTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sdf.format(new Date());
        String result = "";
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            result += random.nextInt(10);
        }
        return newDate + result;
    }

    /**
     * 提交订单保存数据
     */
    @RequestMapping("/order/submitOrder.html")
    public String submitOrder(HttpServletRequest request, HttpServletResponse response, Order order) {
        UserCart userCart = new UserCart();
        String username = localSessionProvider.getAttribute(request, response, Constants.USER_NAME);
        //订单所属用户
        order.setBuyerId(username);
        Jedis jedis = jedisPool.getResource();
        //从缓存中取出购物车数据
        List<String> keys = jedis.lrange("userCart:" + username, 0, -1);
        for (String key : keys) {
            Sku sku = new Sku();
            sku.setId(Long.parseLong(key));
            UserItem userItem = new UserItem();
            userItem.setSku(sku);
            userItem.setAmount(Integer.parseInt(jedis.hget("userItem:" + username, key)));
            userCart.addUserItem(userItem);
        }

        //加载购物车数据
        showCart(userCart);

        //保存订单和订单详情数据
        orderService.insertOrder(order, userCart);

        // 修改商品的库存
        for (String skuId : keys) {
            Sku sku = skuService.loadById(Long.parseLong(skuId));
            //取出购买的数量
            String amount = jedis.hget("userItem:" + username, skuId);
            sku.setStock(sku.getStock() - Integer.parseInt(amount));
            //更新
            skuService.update(sku);
        }

        //清空购物车
        jedis.del("userCart:" + username, "userItem:" + username);
        return "product/confirmOrder";
    }

    /**
     * 加载购物车数据
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

    @RequestMapping("/order/mobileSubmitOrder.html")
    public void submitOrder(Order order, @RequestParam(value="carts[]") Long[] cartIds, /*String[] names,
                            Float[] prices, Integer[] counts, String[] sizes,*/
                            HttpServletResponse response) {
        //订单数据
        Float totalFee = order.getTotalFee();
        order.setOrderPrice(totalFee);
        order.setDelivery(0);
        //默认为待付款
        order.setIsPaiy(1);
        //默认状态为提交订单
        order.setOrderState(0);
        order.setCreateDate(new Date());
        order.setId(Long.parseLong(getOrderIdByTime()));
        //订单详情
        //detail.setColor("测试");
        //orderService.insertMobileOrder(order, detail);

    }

    /**
     * 添加订单
     *
     * @param order
     * @param response
     * @throws IOException
     */

    public void mobileSubmitOrder(Order order, HttpServletResponse response) throws IOException {
        JSONObject json = new JSONObject();
        Float totalFee = order.getTotalFee();
        order.setOrderPrice(totalFee);
        order.setDelivery(0);
        //默认为待付款
        order.setIsPaiy(1);
        //默认状态为提交订单
        order.setOrderState(0);
        order.setCreateDate(new Date());
        //orderService.insertMobileOrder(order);
        json.put("id", order.getId());
        response.getWriter().write(json.toString());
    }

    /**
     * 插入订单详情
     *
     * @param detail
     * @param response
     */
    @RequestMapping("/order/mobileSubmitOrderDetail.html")
    public void mobileSubmitOrderDetail(Detail detail, HttpServletResponse response) {
        detail.setColor("测试");
        orderService.insertOrderDetail(detail);
        JSONObject json = new JSONObject();
        json.put("msg", "订单提交成功");
    }

}
