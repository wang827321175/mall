package service;

import pojo.product.Color;
import pojo.product.ColorCriteria;
import pojo.product.Feature;

import java.util.List;

public interface ColorService {
    List<Color> selectBycolorCriteria(ColorCriteria colorCriteria);

    Color selectByColorId(Long valueOf);
}
