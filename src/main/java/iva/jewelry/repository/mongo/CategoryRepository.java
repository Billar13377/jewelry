package iva.jewelry.repository.mongo;

import iva.jewelry.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {}