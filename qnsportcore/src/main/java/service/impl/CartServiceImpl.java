package service.impl;

import dao.cart.CartMapper;
import dao.product.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pojo.cart.Cart;
import pojo.cart.CartCriteria;
import pojo.product.Product;
import service.CartService;

import java.util.List;

/**
 * @author wang
 */
@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    CartMapper cartMapper;

    @Autowired
    ProductMapper productMapper;


    @Override
    public void addToCart(Cart cart) {
        //根据id查询商品的名称
        Product product = productMapper.selectByPrimaryKey(cart.getId());
        cart.setName(product.getName());

        //判断数量
        Cart inCart = cartMapper.selectByPrimaryKey(cart.getId());
        //未查询到购物车中有该商品则将数量设置为1
        if (inCart==null){
            cart.setCount(1);
            cartMapper.insert(cart);
        }else{
            Integer count = inCart.getCount();
            count++;
            cart.setCount(count);
            cartMapper.updateByPrimaryKey(cart);
        }

    }

    @Override
    public List<Cart> selectByUsername(String username) {
        CartCriteria cartCriteria = new CartCriteria();
        cartCriteria.createCriteria().andUsernameEqualTo(username);
        List<Cart> carts = cartMapper.selectByExample(cartCriteria);
        return carts;
    }

    @Override
    public Cart selectById(Long id) {
        return cartMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateCount(Cart cart) {
        cartMapper.updateByPrimaryKey(cart);
    }

    @Override
    public void deleteCart(CartCriteria cartCriteria) {
        cartMapper.deleteByExample(cartCriteria);
    }
}
