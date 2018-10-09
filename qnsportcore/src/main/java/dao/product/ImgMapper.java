package dao.product;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import pojo.product.Img;
import pojo.product.ImgCriteria;

public interface ImgMapper {
    int countByExample(ImgCriteria example);

    int deleteByExample(ImgCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(Img record);

    int insertSelective(Img record);

    List<Img> selectByExample(ImgCriteria example);

    Img selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Img record, @Param("example") ImgCriteria example);

    int updateByExample(@Param("record") Img record, @Param("example") ImgCriteria example);

    int updateByPrimaryKeySelective(Img record);

    int updateByPrimaryKey(Img record);
}