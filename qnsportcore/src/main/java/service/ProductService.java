package service;

import com.qingniao.common.page.PageInfo;
import org.apache.solr.client.solrj.SolrServerException;
import pojo.product.Product;
import pojo.product.ProductCriteria;

import java.io.IOException;

/**
 * @author wang
 */
public interface ProductService {

    void insertSelective(Product product);

    PageInfo selectByExample(ProductCriteria productCriteria);

    void update(Long[] ids);

    void sellOut(Long[] ids) throws IOException, SolrServerException;

    Product selectByProductId(Long id);

    /**
     * 批量删除
     * @param ids
     */
    void batchDelete(Long[] ids);

    /**
     * 更新商品详情信息
     * @param product
     */
    void updateDetail(Product product);
}
