package dao.cart;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import pojo.cart.Cart;
import pojo.cart.CartCriteria;

public interface CartMapper {
    int countByExample(CartCriteria example);

    int deleteByExample(CartCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(Cart record);

    int insertSelective(Cart record);

    List<Cart> selectByExample(CartCriteria example);

    Cart selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Cart record, @Param("example") CartCriteria example);

    int updateByExample(@Param("record") Cart record, @Param("example") CartCriteria example);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);
}