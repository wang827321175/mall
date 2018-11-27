package dao.order;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import pojo.order.Detail;
import pojo.order.DetailCriteria;

/**
 * @author wang
 */
public interface DetailMapper {
    int countByExample(DetailCriteria example);

    int deleteByExample(DetailCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(Detail record);

    int insertSelective(Detail record);

    List<Detail> selectByExample(DetailCriteria example);

    Detail selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Detail record, @Param("example") DetailCriteria example);

    int updateByExample(@Param("record") Detail record, @Param("example") DetailCriteria example);

    int updateByPrimaryKeySelective(Detail record);

    int updateByPrimaryKey(Detail record);
}