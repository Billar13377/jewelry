package iva.jewelry.repository;

import iva.jewelry.model.Stone;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoneRepository extends MongoRepository<Stone, String> {
    Optional<Stone> findByCode(String code);
}
