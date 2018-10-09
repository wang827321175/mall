package partal.contorller;

import com.qingniao.common.page.PageInfo;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.servlet.http.HttpServletRequest;
import java.sql.Types;
import java.util.*;

/**
 * @author
 */

@Controller
public class ProductController {

    @Autowired
    SolrServer solrServer;

    @Autowired
    JedisPool jedisPool;

    @Autowired
    ProductService productService;

    @Autowired
    TypeService typeService;

    @Autowired
    ColorService colorService;

    @Autowired
    FeatureService featureService;

    @Autowired
    StoreService storeService;

    @Autowired
    BrandService brandService;

    @Autowired
    ImgService imgService;

    @Autowired
    SkuService skuService;


    @RequestMapping("/index.html")
    public String list(Model model, String storeId, Integer pageNo, String pname, Long brandId,
                       HttpServletRequest request, String price)
            throws SolrServerException {
        Jedis jedis = jedisPool.getResource();
        List<Brand> brands = new ArrayList();
        Set<String> keys = jedis.keys("brand*");
        //遍历
        for (String key : keys) {
            Brand brand = new Brand();
            brand.setId(Long.parseLong(jedis.hget(key, "id")));
            brand.setName(jedis.hget(key, "name"));
            brands.add(brand);
        }
        model.addAttribute("brands", brands);

        SolrQuery solrQuery = new SolrQuery();
        StringBuilder stringBuilder = new StringBuilder();
        ProductCriteria productCriteria = new ProductCriteria();
        //设置每页条数
        productCriteria.setPageSize(9);

        //判断当前页是否为空
        if (pageNo != null) {
            productCriteria.setPageNo(pageNo);
            model.addAttribute("pageNo", pageNo);
        } else {
            productCriteria.setPageNo(1);
        }

        //条件显示
        boolean flag = false;
        Map condition = new HashMap(16);

        //判断名称是否为空
        if (pname != null && pname.trim().length() > 0) {
            solrQuery.set("q", "name_ik:" + pname);

            //高亮显示
            //开启高亮
            solrQuery.setHighlight(true);
            //设置需高亮的字段
            solrQuery.addHighlightField("name_ik");
            //设置高亮前后缀
            solrQuery.setHighlightSimplePre("<span style='color:red'>");
            solrQuery.setHighlightSimplePost("</span>");
            stringBuilder.append("pname=" + pname);
            //回显
            model.addAttribute("pname", pname);

        } else {
            solrQuery.set("q", "*:*");
        }

        //判断品牌id
        if (brandId != null) {
            solrQuery.addFilterQuery("brandId:" + brandId);
            model.addAttribute("brandId", brandId);

            condition.put("品牌", jedis.hget("brand" + brandId, "name"));
            flag = true;
        }

        //判断名牌
        if (storeId != null) {
            solrQuery.addFilterQuery("storeId:" + storeId);
            //查询商铺名称并显示在页面
            Store store = storeService.findById(storeId);

            model.addAttribute("store", store);
        }
        //判断价格
        if (price != null) {
            String[] strings = price.split("-");

            if (strings.length == 2) {
                //价格小于600
                Float start = new Float(strings[0]);
                Float end = new Float(strings[1]);
                solrQuery.addFilterQuery("price:[" + start + " TO " + end + "]");
                condition.put("价格", price);
            } else {
                //价格大于600
                Float start = new Float(600);
                Float end = new Float(1000000);
                solrQuery.addFilterQuery("price:[" + start + " TO " + end + "]");
                condition.put("价格", price);
            }
            flag = true;
            //回显价格
            model.addAttribute("price", price);
        }
        //回显页面筛选条件
        model.addAttribute("flag", flag);
        model.addAttribute("condition", condition);
        solrQuery.setStart(productCriteria.getStartRow());
        solrQuery.setRows(productCriteria.getPageSize());
        //排序方式
        solrQuery.setSort("price", SolrQuery.ORDER.asc);

        QueryResponse queryResponse = solrServer.query(solrQuery);
        SolrDocumentList results = queryResponse.getResults();
        List products = new ArrayList();
        //遍历结果
        for (SolrDocument result : results) {
            Product product = new Product();
            //id
            String id = (String) result.get("id");
            product.setId(Long.parseLong(id));


            //从redis取数据,判断name
            if (pname != null && pname.trim().length() > 0) {
                //获取高亮数据
                Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
                Map<String, List<String>> map = highlighting.get(id);
                List<String> list = map.get("name_ik");
                String name = list.get(0);
                product.setName(name);

            } else {
                String name = (String) result.get("name_ik");
                product.setName(name);
            }
            //url
            Img img = new Img();
            img.setUrl((String) result.get("url"));
            product.setImg(img);
            //redis中查出的price
            float pprice = (float) result.get("price");
            product.setPrice(pprice);
            //brandId
            Integer bid = (Integer) result.get("brandId");
            product.setBrandId(Long.parseLong(bid.toString()));
            products.add(product);

        }
        String url = "/index.html";
        PageInfo pageInfo = new PageInfo
                (productCriteria.getPageNo(), productCriteria.getPageSize(), (int) results.getNumFound());
        pageInfo.setList(products);
        pageInfo.pageView(url, stringBuilder.toString());
        model.addAttribute("pageInfo", pageInfo);

        return "product/mobileProduct";
    }


    /**
     * 跳转详情页
     */
    @RequestMapping("/product/detail.html")
    public String productDetail(Long id, String storeId, Model model) {
        Product product = productService.selectByProductId(id);
        //加载sku
        SkuCriteria skuCriteria = new SkuCriteria();
        skuCriteria.createCriteria().andProductIdEqualTo(id);
        List<Sku> skus = skuService.selectByExample(skuCriteria);
        //创建一个set集合保存颜色
        List colors = new ArrayList<>();
        for (Sku sku : skus) {
            if (colors.contains(sku.getColor())) {
                continue;
            } else {
                colors.add(sku.getColor());
            }
        }
        model.addAttribute("skus", skus);
        model.addAttribute("colors", colors);
        model.addAttribute("product", product);


        //查询所有类型
        TypeCriteria typeCriteria = new TypeCriteria();
        List<Type> types = typeService.selectByTypesCriteria(typeCriteria);
        model.addAttribute("types", types);

        //颜色
        ColorCriteria colorCriteria = new ColorCriteria();
        List<Color> allColors = colorService.selectBycolorCriteria(colorCriteria);
        model.addAttribute("allColors", allColors);

        //材质
        FeatureCriteria featureCriteria = new FeatureCriteria();
        List<Feature> features = featureService.selectByfeatureCriteria(featureCriteria);
        model.addAttribute("features", features);

        //品牌
        BrandExample brandExample = new BrandExample();
        List<Brand> brands = brandService.selectByBrandExample(brandExample);
        model.addAttribute("brands", brands);

        model.addAttribute("storeId",storeId);

        return "product/mobileProductDetail";
    }
}
