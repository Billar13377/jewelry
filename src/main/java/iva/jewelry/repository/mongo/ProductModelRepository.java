package iva.jewelry.repository.mongo;

import iva.jewelry.model.ProductModel;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductModelRepository extends MongoRepository<ProductModel, String> {
    List<ProductModel> findByCategoryId(String categoryId);
}