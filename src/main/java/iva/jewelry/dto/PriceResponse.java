package iva.jewelry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PriceResponse {

    private BigDecimal price;

}
