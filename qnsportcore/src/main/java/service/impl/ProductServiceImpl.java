package service.impl;

import com.qingniao.common.page.PageInfo;
import common.FreemarkerUtils;
import dao.product.ImgMapper;
import dao.product.ProductMapper;
import dao.product.SkuMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pojo.product.*;
import service.ProductService;
import service.SkuService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ImgMapper imgMapper;

    @Autowired
    SolrServer solrServer;

    @Autowired
    SkuMapper skuMapper;

    @Autowired
    SkuService skuService;

    @Autowired
    FreemarkerUtils freemarkerUtils;


    @Override
    public void insertSelective(Product product) {
        productMapper.insertSelective(product);
    }

    @Override
    public PageInfo selectByExample(ProductCriteria productCriteria) {
        PageInfo pageInfo = new PageInfo(productCriteria.getPageNo(), productCriteria.getPageSize(), productMapper.countByExample(productCriteria));
        //矫正页面
        productCriteria.setPageNo(pageInfo.getPageNo());
        List<Product> products = productMapper.selectByExample(productCriteria);
        for (Product product : products) {
            ImgCriteria imgCriteria = new ImgCriteria();
            imgCriteria.createCriteria().andProductIdEqualTo(product.getId()).andIsDefEqualTo(false);
            List<Img> imgs = imgMapper.selectByExample(imgCriteria);
            product.setImg(imgs.get(0));
        }
        pageInfo.setList(products);
        return pageInfo;
    }

    /**
     * 上架
     *
     * @param ids
     */
    @Override
    public void update(Long[] ids) {
        for (Long id : ids) {
            Product product = new Product();
            product.setId(id);
            product.setIsShow(true);
            productMapper.updateByPrimaryKeySelective(product);
            Product p = productMapper.selectByPrimaryKey(id);

            //保存商品到solr服务器
            SolrInputDocument doc = new SolrInputDocument();
            //商品id
            doc.setField("id", id);
            //名称
            doc.setField("name_ik", p.getName());
            //url
            ImgCriteria imgCriteria = new ImgCriteria();
            imgCriteria.createCriteria().andProductIdEqualTo(id).andIsDefEqualTo(false);
            List<Img> imgs = imgMapper.selectByExample(imgCriteria);
            doc.setField("url", imgs.get(0).getUrl());
            //获取最低价格
            float price = skuMapper.selectPriceByProductId(id);
            doc.setField("price", price);
            //设置brandId
            doc.setField("brandId", p.getBrandId());
            doc.setField("storeId",p.getStoreId());
            try {
                solrServer.add(doc);
                solrServer.commit();
            } catch (SolrServerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //生成静态页面
            Map root = new HashMap();
            root.put("productId", id);

            Product pro = selectByProductId(id);
            SkuCriteria skuCriteria = new SkuCriteria();
            skuCriteria.createCriteria().andProductIdEqualTo(id);
            List<Sku> skus = skuService.selectByExample(skuCriteria);
            //颜色
            List colors = new ArrayList();
            for (Sku sku : skus) {
                if (colors.contains(sku.getColor())) {
                    continue;
                } else {
                    colors.add(sku.getColor());
                }
            }
            root.put("product", pro);
            root.put("colors", colors);
            root.put("skus", skus);

            //调用工具类
            //freemarkerUtils.toHTML(root);
        }
    }

    /**
     * 下架
     *
     * @param ids
     */
    @Override
    public void sellOut(Long[] ids) throws IOException, SolrServerException {
        for (Long id : ids) {
            Product product = new Product();
            product.setId(id);
            product.setIsShow(false);
            productMapper.updateByPrimaryKeySelective(product);
            solrServer.deleteById(String.valueOf(id));
            solrServer.commit();
        }
    }

    @Override
    public Product selectByProductId(Long id) {
        Product product = productMapper.selectByPrimaryKey(id);
        //查询图片
        ImgCriteria imgCriteria = new ImgCriteria();
        imgCriteria.createCriteria().andProductIdEqualTo(id).andIsDefEqualTo(false);
        List<Img> imgs = imgMapper.selectByExample(imgCriteria);
        product.setImg(imgs.get(0));
        return product;
    }

    @Override
    public void batchDelete(Long[] ids) {
        for (Long id : ids) {
            //因为商品数据存在外键,需要先判断该商品是否有货
            SkuCriteria skuCriteria = new SkuCriteria();
            skuCriteria.createCriteria().andProductIdEqualTo(id);

            ImgCriteria imgCriteria = new ImgCriteria();
            imgCriteria.createCriteria().andProductIdEqualTo(id);

            //删除关联的sku
            skuMapper.deleteByExample(skuCriteria);

            //删除图片
            imgMapper.deleteByExample(imgCriteria);

            //删除商品
            productMapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public void updateDetail(Product product) {
        productMapper.updateByPrimaryKey(product);
    }

}
