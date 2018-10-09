package service;

import com.qingniao.common.page.PageInfo;
import pojo.Brand;
import pojo.BrandExample;

import java.util.List;

public interface BrandService {
    void insertBrand(Brand brand);

    PageInfo selectByExample(BrandExample brandExample);

    void batchDelete(Long[] ids);

    Brand findbyid(Long id);

    void update(Brand brand);

    List<Brand> selectByBrandExample(BrandExample brandExample);
}
