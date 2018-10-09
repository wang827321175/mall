package console.contorller;


import com.qingniao.common.page.PageInfo;
import common.SERVER_URL;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


import pojo.Brand;
import pojo.BrandExample;
import pojo.product.*;
import pojo.store.Store;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import service.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pojo.product.ProductCriteria.Criteria;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * 商品的controller
 *
 * @author
 */
@Controller
public class ProductController {

    @Autowired
    TypeService typeService;

    @Autowired
    BrandService brandService;

    @Autowired
    FeatureService featureService;

    @Autowired
    ColorService colorService;

    @Autowired
    ProductService productService;

    @Autowired
    ImgService imgService;

    @Autowired
    SkuService skuService;

    @Autowired
    StoreService storeService;

    @Autowired
    JedisPool jedisPool;

    /**
     * 分页条件查询
     *
     * @param pageNo
     * @param name
     * @param brandId
     * @param isShow
     * @param model
     * @return
     */
    @RequestMapping("/product/list.do")
    public String productList(HttpServletRequest request, Integer pageNo, String storeId, String name, Long brandId, Boolean isShow, Model model) {
        //加载品牌数据
        BrandExample brandExample = new BrandExample();
        brandExample.setPageNo(1);
        List<Brand> brandList = brandService.selectByBrandExample(brandExample);
        model.addAttribute("brandList", brandList);

        //创建查询对象
        ProductCriteria productCriteria = new ProductCriteria();
        Criteria criteria = productCriteria.createCriteria();

        //制作分页工具栏
        StringBuilder stringBuilder = new StringBuilder();

        //判断当前页
        if (pageNo != null) {
            productCriteria.setPageNo(pageNo);
        } else {
            productCriteria.setPageNo(1);
        }

        //判断上下架状态
        if (isShow != null) {
            criteria.andIsShowEqualTo(isShow);
            stringBuilder.append("isShow=" + isShow);
            //回显是否上架
            model.addAttribute("isShow", isShow);
        } else {
            criteria.andIsShowEqualTo(false);
            stringBuilder.append("isShow=false");
        }

        //判断输入的名称
        if (name != null && name.trim().length() != 0) {
            criteria.andNameLike("%" + name + "%");
            stringBuilder.append("name=" + name);
            //回显商品名称
            model.addAttribute("name", name);
        }

        //判断通过品牌id查询
        if (brandId != null) {
            criteria.andBrandIdEqualTo(brandId);
            stringBuilder.append("&brandId=" + brandId);
            //回显品牌
            model.addAttribute("brandId", brandId);
        }

        if (storeId != null) {
            criteria.andStoreIdEqualTo(storeId);
            stringBuilder.append("&storeId=" + storeId);
            //查询商铺的名称显示在页面
            Store store = storeService.findById(storeId);
            model.addAttribute("store", store);
            //回显商铺id
            model.addAttribute("storeId", storeId);
        }

        //分页工具栏
        String url = "/product/list.do";
        PageInfo pageInfo = productService.selectByExample(productCriteria);
        pageInfo.pageView(url, stringBuilder.toString());

        model.addAttribute("pageInfo", pageInfo);

        return "product/list";
    }

    /**
     * 初始化页面数据,如果参数中带有商品id则是修改商品
     *
     * @param model
     * @return
     */
    @RequestMapping("/product/add.do")
    public String productAdd(Model model, String storeId, Long id, HttpServletRequest request) throws IOException, SolrServerException {
        //商品类型
        TypeCriteria typeCriteria = new TypeCriteria();
        typeCriteria.createCriteria().andParentIdNotEqualTo(0L);
        List<Type> types = typeService.selectByTypesCriteria(typeCriteria);
        model.addAttribute("types", types);

        //商品品牌
        BrandExample brandExample = new BrandExample();
        List<Brand> brands = brandService.selectByBrandExample(brandExample);
        model.addAttribute("brands", brands);

        //材质
        FeatureCriteria featureCriteria = new FeatureCriteria();
        featureCriteria.createCriteria().andIsDelEqualTo(true);
        List<Feature> features = featureService.selectByfeatureCriteria(featureCriteria);
        model.addAttribute("features", features);

        //颜色
        ColorCriteria colorCriteria = new ColorCriteria();
        colorCriteria.createCriteria().andParentIdNotEqualTo(0L);
        List<Color> colors = colorService.selectBycolorCriteria(colorCriteria);
        model.addAttribute("colors", colors);

        //商品所在的商店
        model.addAttribute("storeId", storeId);

        //携带初始化参数到页面
        // 如果传入id  说明是修改商品,跳转商品修改页面
        if (id != null) {

            //材质
            Product product = productService.selectByProductId(id);
            String productFeatures = product.getFeatures();
            String[] pFeatureArray = productFeatures.split(",");
            List<Feature> pFeatures=new ArrayList<>();
            for (int i = 0; i < pFeatureArray.length; i++) {
                Feature feature= featureService.selectByFratureId(Long.valueOf(pFeatureArray[i]));
                pFeatures.add(feature);
            }
            model.addAttribute("pFeatures", pFeatures);

            //获取商品颜色
            String productColors = product.getColors();
            String[] pColorArray = productColors.split(",");
            List<Color> pColors=new ArrayList<>();
            for (int i = 0; i < pColorArray.length; i++) {
                Color color= colorService.selectByColorId(Long.valueOf(pColorArray[i]));
                pColors.add(color);
            }
            model.addAttribute("pColors", pColors);

            //尺寸
            String productSizes = product.getSizes();
            String[] pSizeArray = productSizes.split(",");
            model.addAttribute("pSizeArray", pSizeArray);

            //图片
            Img img = imgService.findByProductId(id);
            String imgUrl = img.getImgUrl();
            model.addAttribute("imgUrl",imgUrl);

            model.addAttribute("product", product);
            return "product/update";
        } else {
            //没有商品id传入,跳转添加页面
            return "product/add";
        }

    }

    /**
     * 保存添加商品
     *
     * @param product
     * @return
     */
    @RequestMapping("/product/save.do")
    public String save(Product product, String storeId, Model model) {
        //用redis的自增获得商品id
        Jedis jedis = jedisPool.getResource();
        Long productId = jedis.incr("productId");
        product.setId(productId);
        //保存商品数据
        //上下架状态
        product.setIsShow(false);
        //是否被删除
        product.setIsDel(false);
        //上架时间
        product.setCreateTime(new Date());
        //插入数据并返回id
        product.setStoreId(storeId);
        productService.insertSelective(product);

        //保存图片
        Img img = product.getImg();
        img.setIsDef(false);
        img.setProductId(productId);
        imgService.update(img);

        //保存sku(最小销售单元)
        String colors = product.getColors();
        String sizes = product.getSizes();

        //遍历颜色和尺码
        for (String color : colors.split(",")) {
            Sku sku = new Sku();
            //颜色id
            sku.setColorId(Long.parseLong(color));
            //商品id
            sku.setProductId(productId);
            //创建时间
            sku.setCreateTime(new Date());
            for (String size : sizes.split(",")) {
                //尺码
                sku.setSize(size);
                //运费
                sku.setDeliveFee(10f);
                //市场售价
                sku.setMarketPrice(150f);
                //售价
                sku.setPrice(99f);
                //库存
                sku.setStock(100);
                //购买限制
                sku.setUpperLimit(100);
                skuService.insertSelective(sku);
            }
        }
        model.addAttribute("storeId", storeId);
        return "redirect:/product/list.do";
    }

    /**
     * 商品上架
     *
     * @param ids
     * @return
     */
    @RequestMapping("/product/onSale.do")
    public String onSale(Long[] ids, String storeId, Model model) {
        productService.update(ids);
        model.addAttribute("storeId", storeId);
        return "redirect:/product/list.do";
    }

    /**
     * 商品下架
     *
     * @param ids
     * @return
     */
    @RequestMapping("/product/sellOut.do")
    public String sellOut(Long[] ids, String storeId, Model model) throws IOException, SolrServerException {
        productService.sellOut(ids);
        model.addAttribute("storeId", storeId);
        return "redirect:/product/list.do";
    }

    /**
     * 删除商品
     */
    @RequestMapping("/product/batchDelete.do")
    public String batchDelete(HttpServletRequest request, Long[] ids, String storeId, Model model) {
        productService.batchDelete(ids);
        model.addAttribute("storeId", storeId);
        return "redirect:/product/list.do";
    }

    /**
     * 修改商品
     *
     * @param product
     * @param storeId
     * @param model
     * @return
     */
    @RequestMapping("/product/update")
    public String update(Product product, String storeId, Model model) throws IOException, SolrServerException {
        //修改商品之前下架商品
        Long id = product.getId();
        Long[] ids={id};
        sellOut(ids,storeId,model);

        //保存商品数据
        //上下架状态
        product.setIsShow(false);
        //是否被删除
        product.setIsDel(false);
        //上架时间
        product.setCreateTime(new Date());
        //插入数据并返回id
        product.setStoreId(storeId);
        productService.updateDetail(product);

        //保存图片
        Img img = product.getImg();
        if (img.getImgUrl()!=null) {
            Img oldImg = imgService.findByProductId(id);
            //新的url替换掉旧的
            oldImg.setUrl(img.getUrl());
            img.setIsDef(false);
            img.setProductId(product.getId());
            imgService.update(oldImg);
        }
        //保存sku(最小销售单元)
        String colors = product.getColors();
        String sizes = product.getSizes();

        //遍历颜色和尺码
        for (String color : colors.split(",")) {
            Sku sku = new Sku();
            //颜色id
            sku.setColorId(Long.parseLong(color));
            //商品id
            sku.setProductId(product.getId());
            //创建时间
            sku.setCreateTime(new Date());
            for (String size : sizes.split(",")) {
                //尺码
                sku.setSize(size);
                //运费
                sku.setDeliveFee(10f);
                //市场售价
                sku.setMarketPrice(150f);
                //售价
                sku.setPrice(99f);
                //库存
                sku.setStock(100);
                //购买限制
                sku.setUpperLimit(100);
                skuService.update(sku);
            }
        }
        model.addAttribute("storeId", storeId);
        return "redirect:/product/list.do";
    }


}
