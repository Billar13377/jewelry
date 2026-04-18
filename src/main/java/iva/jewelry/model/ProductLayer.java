package iva.jewelry.model;

import iva.jewelry.dto.LayerType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("product_layers")
@Data
public class ProductLayer {

    @Id
    private String id;

    private String modelId;

    private LayerType type;

    private String material;

    private String stone;

    private String shape;

    private Double size;

    private String imageUrl;
}

