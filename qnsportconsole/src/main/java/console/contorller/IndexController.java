package console.contorller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wang
 */
@Controller
public class IndexController{

   /* @RequestMapping("/login.do")
    public String login(){
        return "login";
    }*/

    @RequestMapping("/index.do")
    public String index() {
        return "index";
    }

    @RequestMapping("/top.do")
    public String top() {
        return "top";
    }

    @RequestMapping("/main.do")
    public String main() {
        return "main";
    }

    @RequestMapping("/left.do")
    public String left() {
        return "left";
    }

    @RequestMapping("/right.do")
    public String right() {
        return "right";
    }

    /**
     * 跳转商品页面
     * @return
     */
    @RequestMapping("/productMain.do")
    public String productMain() {
        return "product/product_main";
    }


    /**
     * 加载商品页面的左边菜单
     * @return
     */
    @RequestMapping("/productLeft.do")
    public String productLeft() {
        return "product/product_left";
    }

    /**
     * 加载订单页面
     */
    @RequestMapping("/orderMain.do")
    public String orderMain() {
        return "order/order_main";
    }


    @RequestMapping("/orderLeft.do")
    public String orderLeft() {
        return "order/order_left";
    }

    @RequestMapping("/order/list.do")
    public String orderList() {
        return "order/list";
    }

}
