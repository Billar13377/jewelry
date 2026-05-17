package iva.jewelry.repository.mongo;

import iva.jewelry.model.ProductLayer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductLayerRepository
        extends MongoRepository<ProductLayer, String> {

    List<ProductLayer> findByModelId(String modelId);

}