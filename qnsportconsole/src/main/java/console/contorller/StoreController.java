package console.contorller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pojo.store.Store;
import service.StoreService;

import java.util.List;

/**
 * @author wang
 * @date 2018/09/01
 */
@Controller
public class StoreController {

    @Autowired
    private StoreService storeService;

    /**
     * 展示商铺列表
     * @param model
     * @return
     */
    @RequestMapping("/store/list.do")
    public String storeList(Model model){
        List<Store> stores=storeService.findAll();
        model.addAttribute(stores);
        return "store/list";
    }

    /**
     * 跳转添加页面
     * @return
     */
    @RequestMapping("/store/add.do")
    public String add(){
        return "store/add";
    }

    /**
     * 保存商铺
     * @param name
     * @param description
     * @return
     */
    @RequestMapping("/store/save.do")
    public String save(String name,String description){
        Store store = new Store();
        store.setName(name);
        store.setDescription(description);
        storeService.save(store);
        return "redirect:/store/list.do";
    }

    /**
     * 删除店铺
     * @param ids
     * @return
     */
    @RequestMapping("/store/batchDelete.do")
    public String batchDelete(String[] ids){
        storeService.batchDelete(ids);
        return "redirect:/store/list.do";
    }

    @RequestMapping("store/toUpdatePage.do")
    public String toUpdatePage(Model model,String storeId){
        Store store=storeService.findById(storeId);
        model.addAttribute("store",store);
        return "store/add";
    }
}
