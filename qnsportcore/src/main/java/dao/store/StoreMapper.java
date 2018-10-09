package dao.store;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import pojo.store.Store;
import pojo.store.StoreCriteria;

public interface StoreMapper {
    int countByExample(StoreCriteria example);

    int deleteByExample(StoreCriteria example);

    int deleteByPrimaryKey(String id);

    int insert(Store record);

    int insertSelective(Store record);

    List<Store> selectByExample(StoreCriteria example);

    Store selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Store record, @Param("example") StoreCriteria example);

    int updateByExample(@Param("record") Store record, @Param("example") StoreCriteria example);

    int updateByPrimaryKeySelective(Store record);

    int updateByPrimaryKey(Store record);

    List<Store> findAll();
}