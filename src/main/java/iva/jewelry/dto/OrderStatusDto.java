package iva.jewelry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusDto {
    private Integer statusId;
    private String name;
    private Date dateOfChanging;
}
