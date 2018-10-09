package dao.user;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import pojo.user.Addr;
import pojo.user.AddrCriteria;

public interface AddrMapper {
    int countByExample(AddrCriteria example);

    int deleteByExample(AddrCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(Addr record);

    int insertSelective(Addr record);

    List<Addr> selectByExample(AddrCriteria example);

    Addr selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Addr record, @Param("example") AddrCriteria example);

    int updateByExample(@Param("record") Addr record, @Param("example") AddrCriteria example);

    int updateByPrimaryKeySelective(Addr record);

    int updateByPrimaryKey(Addr record);
}