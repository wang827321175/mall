package service;

import pojo.product.Sku;
import pojo.product.SkuCriteria;

import java.util.List;

public interface SkuService {
    void insertSelective(Sku sku);

    List<Sku> selectByExample(SkuCriteria skuCriteria);

    void update(Sku sku);

    List<Sku> selectByProductId(Long id);

    Sku loadById(Long id);

    float selectPriceByProductId(Long id);

    /**
     * 查询商品库存
     * @param cid
     * @return
     */
    int selectUpperLimitByProductId(Long cid);
}
