package iva.jewelry.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialVariant {

    private Integer purity;

    private BigDecimal pricePerGram;
}
