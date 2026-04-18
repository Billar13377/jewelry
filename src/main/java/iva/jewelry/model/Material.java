package iva.jewelry.model;

import iva.jewelry.dto.MaterialVariant;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("material_catalog")
@Data
public class Material {
    @Id
    private String code;
    private String name;
    private List<MaterialVariant> variants;
}