package dao.product;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import pojo.product.Type;
import pojo.product.TypeCriteria;

public interface TypeMapper {
    int countByExample(TypeCriteria example);

    int deleteByExample(TypeCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(Type record);

    int insertSelective(Type record);

    List<Type> selectByExample(TypeCriteria example);

    Type selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Type record, @Param("example") TypeCriteria example);

    int updateByExample(@Param("record") Type record, @Param("example") TypeCriteria example);

    int updateByPrimaryKeySelective(Type record);

    int updateByPrimaryKey(Type record);
}