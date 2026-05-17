package iva.jewelry.repository.jpa;

import iva.jewelry.model.Order;
import iva.jewelry.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @EntityGraph(attributePaths = {"orderProducts", "orderStatuses"})
    List<Order> findByUser(User user);
    Optional<Order> findById(Long id);
    List<Order> findByUserId(Long userId);
}
