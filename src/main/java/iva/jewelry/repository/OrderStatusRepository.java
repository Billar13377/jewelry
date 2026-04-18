package iva.jewelry.repository;

import iva.jewelry.model.OrderStatus;
import iva.jewelry.model.OrderStatusId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, OrderStatusId> {
    boolean existsById(OrderStatusId id);
}
