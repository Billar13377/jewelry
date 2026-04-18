package iva.jewelry.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Document("stone_catalog")
@Data
public class Stone {
    @Id
    private String code;
    private String name;
    private BigDecimal pricePerCarat;
    private List<String> shapes;
    private List<Double> sizes;
}
