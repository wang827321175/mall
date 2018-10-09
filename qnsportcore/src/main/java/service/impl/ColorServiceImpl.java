package service.impl;

import dao.product.ColorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pojo.product.Color;
import pojo.product.ColorCriteria;
import pojo.product.Feature;
import service.ColorService;

import java.util.List;

@Service
public class ColorServiceImpl implements ColorService {

    @Autowired
    ColorMapper colorMapper;

    @Override
    public List<Color> selectBycolorCriteria(ColorCriteria colorCriteria) {
        return colorMapper.selectByExample(colorCriteria);
    }

    @Override
    public Color selectByColorId(Long id) {
        return colorMapper.selectByPrimaryKey(id);
    }
}
