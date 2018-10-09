package service;

import pojo.cart.Cart;
import pojo.cart.CartCriteria;

import java.util.List;

/**
 * @author wang
 */
public interface CartService {
    /**
     * 保存商品到购物车
     * @param cart
     */
    void addToCart(Cart cart);

    /**
     * 查找该用户购物车中商品
     * @param username
     * @return
     */
    List<Cart> selectByUsername(String username);

    /**
     * 根据id查找
     * @param id
     * @return
     */
    Cart selectById(Long id);

    /**
     * 修改数据库中购物车商品的数量
     * @param cart
     */
    void updateCount(Cart cart);

    /**
     * 删除购物车中商品
     * @param cartCriteria
     */
    void deleteCart(CartCriteria cartCriteria);
}
