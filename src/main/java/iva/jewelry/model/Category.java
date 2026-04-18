package iva.jewelry.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("categories")
@Data
public class Category {
    @Id
    private String id;

    private String name;

    private String code;

    private String image;
}
