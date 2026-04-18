package iva.jewelry.dto;

import iva.jewelry.model.CartProduct;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class CartSummary {
    private List<CartProduct> items;
    private BigDecimal total;
}