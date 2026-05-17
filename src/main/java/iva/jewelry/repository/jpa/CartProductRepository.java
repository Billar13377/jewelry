package iva.jewelry.repository.jpa;

import iva.jewelry.model.CartProduct;
import iva.jewelry.model.User;
import org.hibernate.annotations.Index;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Long> {

    List<CartProduct> findByUser(User user);
    List<CartProduct> findByUserId(Integer userId);

    @Index(name = "idx_cart_user_hash")
    Optional<CartProduct> findByUserAndSnapshotHash(User user, String hash);
    void deleteByUser(User user);
}