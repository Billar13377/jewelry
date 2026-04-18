package iva.jewelry.dto;

import lombok.Data;

@Data
public class AddToCartRequest {

    private ProductSnapshot snapshot;

    private Integer quantity;
}
