package service.impl;

import dao.product.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pojo.product.Type;
import pojo.product.TypeCriteria;
import service.TypeService;

import java.util.List;

/**
 * @author wnag
 */
@Service
@Transactional
public class TypeServiceImpl implements TypeService {

    @Autowired
    TypeMapper typeMapper;

    @Override
    public List<Type> selectByTypesCriteria(TypeCriteria typeCriteria) {
        return typeMapper.selectByExample(typeCriteria);
    }

}
