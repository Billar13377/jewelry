package iva.jewelry.dto;

import iva.jewelry.model.OrderProduct;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class OrderSummary {
    private Long orderId;
    private Integer userId;
    private String userEmail;
    private Date dateOfCreation;
    private Date dateExpected;
    private BigDecimal totalPrice;
    private List<OrderProduct> items;
    private Set<OrderStatusDto> orderStatuses;
}