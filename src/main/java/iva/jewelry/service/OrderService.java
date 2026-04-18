package iva.jewelry.service;

import iva.jewelry.model.*;
import iva.jewelry.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserRepository userRepository;

    public Order createOrder() {
        User user = getAuthenticatedUser();
        List<CartProduct> cartItems =
                cartRepository.findByUser(user);

        Order order = new Order();
        order.setUser(user);
        order.setDateOfCreation(new Date());
        order.setDateExpected(new Date(System.currentTimeMillis() + 15 * 24 * 60 * 60 * 1000));
        orderRepository.save(order);

        BigDecimal total = BigDecimal.ZERO;

        for (CartProduct cart : cartItems) {

            OrderProduct item = new OrderProduct();

            item.setOrder(order);
            item.setProductSnapshot(cart.getProductSnapshot());
            item.setAmount(cart.getAmount());

            BigDecimal itemTotal = cart.getTotalPrice();
            total = total.add(itemTotal);

            orderItemRepository.save(item);

        }
        order.setTotalPrice(total);

        Status createdStatus = statusRepository.findByName("Создан")
                .orElseThrow(() -> new IllegalStateException("Статус 'Создан' не найден"));
        OrderStatus orderStatus = new OrderStatus(new OrderStatusId(order.getId(), createdStatus.getId()), new Date(), order, createdStatus);
        orderStatusRepository.save(orderStatus);

        cartRepository.deleteAll(cartItems);

        return order;
    }

    public List<Order> getUserOrders() {
        User user = getAuthenticatedUser();
        return orderRepository.findByUser(user);
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

    private User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return userRepository.findByEmail(((UserDetails) principal).getUsername())
                    .orElseThrow(() -> new IllegalStateException("Пользователь не найден"));
        } else {
            throw new IllegalStateException("Ошибка аутентификации");
        }
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
