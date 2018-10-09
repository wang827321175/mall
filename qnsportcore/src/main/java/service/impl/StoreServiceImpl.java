package service.impl;

import dao.store.StoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pojo.store.Store;
import pojo.store.StoreCriteria;
import service.StoreService;

import java.util.List;
import java.util.UUID;

/**
 * @author wang
 */
@Service
@Transactional
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreMapper storeMapper;

    @Override
    public List<Store> findAll() {
        List<Store> stores = storeMapper.findAll();
        return stores;
    }

    @Override
    public void save(Store store) {
        //商铺的id采用uuid生成
        String id = UUID.randomUUID().toString().replace("-", "");
        store.setId(id);
        storeMapper.insert(store);
    }

    @Override
    public void batchDelete(String[] ids) {
        for (int i = 0; i < ids.length; i++) {
            //根据主键删除
            storeMapper.deleteByPrimaryKey(ids[i]);
        }
    }

    @Override
    public Store findById(String storeId) {
        Store store = storeMapper.selectByPrimaryKey(storeId);
        return store;
    }
}
