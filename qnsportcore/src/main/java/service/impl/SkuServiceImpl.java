package service.impl;

import com.qingniao.common.page.PageInfo;
import dao.product.ColorMapper;
import dao.product.ImgMapper;
import dao.product.ProductMapper;
import dao.product.SkuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pojo.product.*;
import service.ColorService;
import service.SkuService;

import java.util.List;

@Service
@Transactional
public class SkuServiceImpl implements SkuService {

    @Autowired
    SkuMapper skuMapper;

    @Autowired
    ColorMapper colorMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ImgMapper imgMapper;

    @Override
    public void insertSelective(Sku sku) {
        skuMapper.insertSelective(sku);
    }

    @Override
    public List<Sku> selectByExample(SkuCriteria skuCriteria) {
        List<Sku> skus = skuMapper.selectByExample(skuCriteria);
        for (Sku sku : skus) {
            ColorCriteria colorCriteria = new ColorCriteria();
            colorCriteria.createCriteria().andIdEqualTo(sku.getColorId());
            List<Color> colors = colorMapper.selectByExample(colorCriteria);
            sku.setColor(colors.get(0));
        }
        return skus;
    }

    @Override
    public void update(Sku sku) {
        skuMapper.updateByPrimaryKeySelective(sku);
    }

    @Override
    public List<Sku> selectByProductId(Long id) {
        return skuMapper.selectByProductId(id);
    }

    /**
     * 通过skuId初始化对象
     *
     * @param id
     * @return
     */
    @Override
    public Sku loadById(Long id) {
        Sku sku = skuMapper.selectByPrimaryKey(id);
        //关联颜色
        sku.setColor(colorMapper.selectByPrimaryKey(sku.getColorId()));

        Product product = productMapper.selectByPrimaryKey(sku.getProductId());
        //关联图片
        ImgCriteria imgCriteria = new ImgCriteria();
        imgCriteria.createCriteria().andProductIdEqualTo(product.getId()).andIsDefEqualTo(false);
        List<Img> imgs = imgMapper.selectByExample(imgCriteria);
        if (imgs.size() > 0) {
            product.setImg(imgs.get(0));
        }
        //关联商品
        sku.setProduct(product);
        return sku;
    }

    @Override
    public float selectPriceByProductId(Long id) {
        return skuMapper.selectPriceByProductId(id);
    }

}
