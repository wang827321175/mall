package console.contorller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pojo.order.Order;
import pojo.order.OrderCriteria;
import pojo.order.OrderCriteria.Criteria;
import service.OrderService;

import java.util.List;

/**
 * 后台订单模块
 *
 * @author
 */
@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

    /**
     * isPaiy       支付状态:0货到付款 1待付款 2已付款 3待退款 4退款成功 5退款失败
     * orderState   订单状态
     *
     * @return
     */
    //@RequestMapping("/order/list.do")
    public String list(Model model, Integer isPaiy, Integer orderState) {
        //封装查询条件
        OrderCriteria orderCriteria = new OrderCriteria();
        Criteria criteria = orderCriteria.createCriteria();
        if (isPaiy != null) {
            criteria.andIsPaiyEqualTo(isPaiy);
        }
        if (orderState != null) {
            criteria.andOrderStateEqualTo(orderState);
        }
        //到数据库查询
        List<Order> orders = orderService.getList(orderCriteria);

        model.addAttribute("orders", orders);
        return "order/list";
    }
}
