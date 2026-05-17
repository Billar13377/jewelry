package iva.jewelry.service;

import iva.jewelry.model.*;
import iva.jewelry.repository.jpa.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderItemRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final CartProductRepository cartRepository;
    private final StatusRepository statusRepository;
    private final AuthContextService authContextService;

@Transactional
public Order createOrder() {
    User user = authContextService.getCurrentUser();
    List<CartProduct> cartItems = cartRepository.findByUser(user);

    if (cartItems == null || cartItems.isEmpty()) {
        throw new IllegalStateException("Корзина пуста. Добавьте товары перед оформлением заказа");
    }

    Order order = new Order();
    order.setUser(user);
    order.setDateOfCreation(new Date());
    order.setDateExpected(new Date(System.currentTimeMillis() + 15 * 24 * 60 * 60 * 1000L));
    order.setTotalPrice(BigDecimal.ZERO);
    order = orderRepository.save(order);

    BigDecimal total = BigDecimal.ZERO;

    for (CartProduct cart : cartItems) {
        OrderProduct item = new OrderProduct();
        item.setOrder(order);
        item.setProductSnapshot(cart.getProductSnapshot());
        item.setAmount(cart.getAmount());

        BigDecimal itemTotal = cart.getTotalPrice();
        if (itemTotal != null) {
            total = total.add(itemTotal);
        }

        orderItemRepository.save(item);
    }

    order.setTotalPrice(total);
    order = orderRepository.save(order);

    Status createdStatus = statusRepository.findByName("Создан")
            .orElseThrow(() -> new IllegalStateException("Статус 'Создан' не найден"));

    OrderStatus orderStatus = new OrderStatus();
    orderStatus.setId(new OrderStatusId(order.getId(), createdStatus.getId()));
    orderStatus.setDateOfChanging(new Date());
    orderStatus.setOrder(order);
    orderStatus.setStatus(createdStatus);
    orderStatusRepository.save(orderStatus);

    cartRepository.deleteAll(cartItems);

    return order;
}

    @Transactional(readOnly = true)
    public List<Order> getUserOrders() {
        User user = authContextService.getCurrentUser();
        List<Order> orders = orderRepository.findByUser(user);
        orders.forEach(o -> {
            Hibernate.initialize(o.getOrderProducts());
            Hibernate.initialize(o.getOrderStatuses());
        });
        return orders;
    }

    @Transactional
    public void updateOrderStatus(Integer orderId, Integer statusId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("Заказ не найден"));

        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new IllegalStateException("Статус не найден"));

        OrderStatusId id = new OrderStatusId(order.getId(), status.getId());

        boolean alreadyExists = orderStatusRepository.existsById(id);
        if (alreadyExists) {
            throw new IllegalStateException("Этот статус уже установлен для заказа");
        }

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setId(id);
        orderStatus.setOrder(order);
        orderStatus.setStatus(status);
        orderStatus.setDateOfChanging(new Date());

        orderStatusRepository.save(orderStatus);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    public List<Order> getOrdersByUser (Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Status> getAllStatuses() {
        return statusRepository.findAll();
    }
}
