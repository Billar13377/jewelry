package iva.jewelry.repository;

import iva.jewelry.model.Material;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterialRepository extends MongoRepository<Material, String> {
    Optional<Material> findByCode(String code);
}
