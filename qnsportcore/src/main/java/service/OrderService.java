package service;

import pojo.cart.UserCart;
import pojo.order.Detail;
import pojo.order.Order;
import pojo.order.OrderCriteria;

import java.util.List;

/**
 * 订单模块
 */


public interface OrderService {
    public void insertOrder(Order order, UserCart userCart);

    List<Order> getList(OrderCriteria criteria);

    /**
     * 添加订单
     *
     * @param order
     */
    void insertMobileOrder(Order order,Detail detail);

    void insertOrderDetail(Detail detail);
}
