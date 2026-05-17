package iva.jewelry.controller;

import iva.jewelry.dto.OrderStatusDto;
import iva.jewelry.dto.OrderSummary;
import iva.jewelry.model.Order;
import iva.jewelry.model.OrderProduct;
import iva.jewelry.model.Status;
import iva.jewelry.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public Order createOrder() {
        return orderService.createOrder();
    }

    @GetMapping("/")
    @Transactional(readOnly = true)
    public List<OrderSummary> getOrders() {
        List<Order> orders = orderService.getUserOrders();
        return orders.stream()
                .map(o -> {
                    List<OrderProduct> items = o.getOrderProducts();
                    Set<OrderStatusDto> statuses = o.getOrderStatuses().stream()
                            .map(s -> new OrderStatusDto(s.getStatus().getId() ,s.getStatus().getName(), s.getDateOfChanging()))
                            .collect(Collectors.toSet());
                   return new OrderSummary(o.getId(), o.getUser().getId(), o.getUser().getEmail(), o.getDateOfCreation(),
                            o.getDateExpected(), o.getTotalPrice(),
                            items, statuses);
                })
                .toList();
    }

    @GetMapping("/admin/statuses")
    public List<Status> getStatuses() {
        return orderService.getAllStatuses();
    }

    @PutMapping("/admin/{orderId}/status/{statusId}")
    public void updateStatus(@PathVariable Integer orderId,
                             @PathVariable Integer statusId) {
        orderService.updateOrderStatus(orderId, statusId);
    }

    @GetMapping("/admin")
    public List<OrderSummary> getAllOrders(@RequestParam(required = false) Long userId) {
        List<Order> orders = orderService.getAllOrders();

        return orders.stream()
                .map(o -> {
                    List<OrderProduct> items = o.getOrderProducts();

                    Set<OrderStatusDto> statuses = o.getOrderStatuses().stream()
                            .map(s -> new OrderStatusDto(s.getStatus().getId() ,s.getStatus().getName(), s.getDateOfChanging()))
                            .collect(Collectors.toSet());
                    return new OrderSummary(
                            o.getId(),
                            o.getUser().getId(),
                            o.getUser().getEmail(),
                            o.getDateOfCreation(),
                            o.getDateExpected(),
                            o.getTotalPrice(),
                            items,
                            statuses
                    );
                })
                .toList();
    }

    @GetMapping("/admin/by-user/{userId}")
    public List<OrderSummary> getByUser(@PathVariable Long userId) {
        return orderService.getOrdersByUser(userId).stream()
                .map(o -> new OrderSummary(
                        o.getId(),
                        o.getUser().getId(),
                        o.getUser().getEmail(),
                        o.getDateOfCreation(),
                        o.getDateExpected(),
                        o.getTotalPrice(),
                        o.getOrderProducts(),
                        o.getOrderStatuses().stream()
                                .map(s -> new OrderStatusDto(
                                        s.getStatus().getId(),
                                        s.getStatus().getName(),
                                        s.getDateOfChanging()
                                ))
                                .collect(Collectors.toSet())
                ))
        .toList();
    }
}

