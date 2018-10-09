package service.impl;

import com.qingniao.common.page.PageInfo;
import dao.BrandMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pojo.Brand;
import pojo.BrandExample;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import service.BrandService;

import java.util.List;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    @Autowired
    BrandMapper brandMapper;

    @Autowired
    JedisPool jedisPool;

    @Override
    public void insertBrand(Brand brand) {
        brandMapper.insertBrand(brand);
        Jedis jedis = jedisPool.getResource();
        jedis.hset("brand" + brand.getId(), "id", brand.getId().toString());
        jedis.hset("brand" + brand.getId(), "name", brand.getName());
        jedis.close();
    }

    @Override
    public PageInfo selectByExample(BrandExample brandExample) {

        PageInfo pageInfo = new PageInfo(brandExample.getPageNo(), brandExample.getPageSize(), brandMapper.selectcount(brandExample));
        brandExample.setPageNo(pageInfo.getPageNo());

        List<Brand> brands = brandMapper.selectByExample(brandExample);
        pageInfo.setList(brands);

        return pageInfo;
    }

    @Override
    public void batchDelete(Long[] ids) {
        Jedis jedis = jedisPool.getResource();
        for (Long id : ids) {
            jedis.del("brand"+id);
        }
        jedis.close();
        brandMapper.batchDelete(ids);
    }

    @Override
    public Brand findbyid(Long id) {
        return brandMapper.findbyid(id);

    }

    @Override
    public void update(Brand brand) {
        Jedis jedis = jedisPool.getResource();
        jedis.hset("brand" + brand.getId(), "id", brand.getId().toString());
        jedis.hset("brand" + brand.getId(), "name", brand.getName());
        jedis.close();
        brandMapper.update(brand);
    }

    @Override
    public List<Brand> selectByBrandExample(BrandExample brandExample) {
        return brandMapper.selectByBrandExample(brandExample);
    }
}
