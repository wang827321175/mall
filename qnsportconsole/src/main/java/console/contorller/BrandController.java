package console.contorller;

import com.qingniao.common.page.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pojo.Brand;
import pojo.BrandExample;
import service.BrandService;


/**
 * @author
 */
@Controller
public class BrandController {

    @Autowired
    private BrandService brandService;


    @RequestMapping("/brand/list.do")
    private String brandList(String name, Integer status, Integer pageNo, Model model) {
        // 分页工具栏
        StringBuffer stringBuffer = new StringBuffer();
        BrandExample brandExample = new BrandExample();
        if (name != null && name.trim().length() != 0) {
            brandExample.setName(name);
            model.addAttribute("name", name);
            stringBuffer.append("name=" + name);
        }

        if (status != null) {
            brandExample.setStatus(status);
            model.addAttribute("status", status);
            stringBuffer.append("&status=" + status);
        }

        if (pageNo != null) {
            brandExample.setPageNo(pageNo);
        } else {
            brandExample.setPageNo(1);
        }
        PageInfo pageInfo = brandService.selectByExample(brandExample);
        model.addAttribute("pageNo", pageInfo.getPageNo());
        pageInfo.pageView("/brand/list.do", stringBuffer.toString());
        model.addAttribute("pageInfo", pageInfo);
        return "brand/list";
    }


    /**
     * 跳转到add页面
     *
     * @return
     */
    @RequestMapping("brand/add.do")
    private String brandAdd() {
        return "brand/add";
    }


    /**
     * brand添加
     *
     * @param brand
     * @return
     */
    @RequestMapping("/brand/save.do")
    private String brandSave(Brand brand) {
        brandService.insertBrand(brand);
        return "redirect:/brand/list.do";
    }

    /**
     * 批量删除
     *
     * @param ids
     * @param id
     * @param name
     * @param status
     * @param pageNo
     * @param model
     * @return
     */
    @RequestMapping("/batchDelete.do")
    public String batchDelete(Long[] ids, Long id, String name, Integer status, Integer pageNo, Model model) {

        if (name != null) {
            model.addAttribute("name", name);
        }
        if (pageNo != null) {
            model.addAttribute("status", status);
        }
        if (name != null) {
            model.addAttribute("pageNo", pageNo);
        }
        if (id != null) {
            ids = new Long[]{id};
        }
        brandService.batchDelete(ids);
        return "redirect:/brand/list.do";
    }

    /**
     * 跳转删除页面
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/editBrand.do")
    public String editBrand(Long id, Model model) {
        Brand brand = brandService.findbyid(id);
        model.addAttribute("brand", brand);
        return "brand/edit";
    }

    /**
     * 保存修改
     *
     * @param brand
     * @return
     */
    @RequestMapping("/brand/edit.do")
    public String brandEdit(Brand brand) {
        brandService.update(brand);
        return "redirect:/brand/list.do";
    }

}
