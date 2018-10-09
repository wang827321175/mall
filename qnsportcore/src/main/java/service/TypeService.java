package service;

import pojo.product.Type;
import pojo.product.TypeCriteria;

import java.util.List;

/**
 * @author
 */
public interface TypeService {
    /**
     * 通过封装条件查询
     * @param typeCriteria
     * @return
     */
    List<Type> selectByTypesCriteria(TypeCriteria typeCriteria);

}
