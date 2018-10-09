package service;

import pojo.product.Img;

public interface ImgService {

    void insertSelective(Img img);

    /**
     * 根据商品id查询图片
     * @param id
     * @return
     */
    Img findByProductId(Long id);

    /**
     * 更新图片
     * @param img
     */
    void update(Img img);
}
