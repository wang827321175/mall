package service.impl;

import dao.product.FeatureMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pojo.product.Feature;
import pojo.product.FeatureCriteria;
import service.FeatureService;

import java.util.List;

@Service
public class FeatureServiceImpl implements FeatureService {

    @Autowired
    FeatureMapper featureMapper;

    @Override
    public List<Feature> selectByfeatureCriteria(FeatureCriteria featureCriteria) {
      return  featureMapper.selectByExample(featureCriteria);
    }

    @Override
    public Feature selectByFratureId(Long id) {
        return featureMapper.selectByPrimaryKey(id);
    }
}
