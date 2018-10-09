package console.contorller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pojo.product.Sku;
import pojo.product.SkuCriteria;
import service.SkuService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 商品sku的controller
 *
 * @author
 */
@Controller
public class SkuController {

    @Autowired
    SkuService skuService;

    /**
     * 跳转库存页
     *
     * @param productId
     * @param model
     * @return
     */
    @RequestMapping("/sku/list.do")
    public String skuList(Long productId, Model model) {
        SkuCriteria skuCriteria = new SkuCriteria();
        skuCriteria.createCriteria().andProductIdEqualTo(productId);
        List<Sku> skus = skuService.selectByExample(skuCriteria);
        model.addAttribute("skus", skus);

        return "sku/list";
    }

    /**
     *
     * @param sku
     * @param response
     */
    @RequestMapping("/sku/update.do")
    public void update(Sku sku, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        skuService.update(sku);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message","保存成功!");
        response.getWriter().write(jsonObject.toString());
    }
}
