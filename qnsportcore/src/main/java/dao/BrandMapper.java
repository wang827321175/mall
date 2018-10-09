package dao;


import pojo.Brand;
import pojo.BrandExample;

import java.util.List;

public interface BrandMapper {

    void insertBrand(Brand brand);

    int selectcount(BrandExample brandExample);

    List<Brand> selectByExample(BrandExample brandExample);

    void batchDelete(Long[] ids);

    Brand findbyid(Long id);

    void update(Brand brand);

    List<Brand> selectByBrandExample(BrandExample brandExample);
}
