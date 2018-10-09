package service;

import pojo.product.Feature;
import pojo.product.FeatureCriteria;

import java.util.List;

public interface FeatureService {
    List<Feature> selectByfeatureCriteria(FeatureCriteria featureCriteria);

    Feature selectByFratureId(Long valueOf);
}
