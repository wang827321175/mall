package service;

import pojo.store.Store;

import java.util.List;

/**
 * @author wang
 */
public interface StoreService {

    /**
     * 查询所有的商铺
     * @return
     */
    List<Store> findAll();

    /**
     * 添加保存店铺
     * @param store
     */
    void save(Store store);

    /**
     * 批量删除商铺
     * @param ids
     */
    void batchDelete(String[] ids);

    /**
     * 根据id查询指定的商铺
     * @param storeId
     * @return
     */
    Store findById(String storeId);
}
