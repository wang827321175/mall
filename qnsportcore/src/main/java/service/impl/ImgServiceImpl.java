package service.impl;

import dao.product.ImgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pojo.product.Img;
import pojo.product.ImgCriteria;
import service.ImgService;

import java.util.List;

/**
 * @author wang
 */
@Service
@Transactional
public class ImgServiceImpl implements ImgService {

    @Autowired
    ImgMapper imgMapper;

    @Override
    public void insertSelective(Img img) {
        imgMapper.insertSelective(img);
    }

    @Override
    public Img findByProductId(Long id) {
        ImgCriteria imgCriteria = new ImgCriteria();
        imgCriteria.createCriteria().andProductIdEqualTo(id);
        List<Img> imgs = imgMapper.selectByExample(imgCriteria);
        Img img = imgs.get(0);
        return img;
    }

    @Override
    public void update(Img img) {
        ImgCriteria imgCriteria = new ImgCriteria();
        imgCriteria.createCriteria().andIdEqualTo(img.getId());
        imgMapper.updateByExampleSelective(img,imgCriteria);
    }
}
