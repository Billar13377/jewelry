package iva.jewelry.model;


import iva.jewelry.dto.StoneVariant;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Document("product_models")
@Data
public class ProductModel {
    @Id
    private String id;
    private String name;
    private String categoryId;
    private BigDecimal basePrice;
    private List<String> materials;
    private BigDecimal weight;
    private Boolean hasMainStone;
    private List<StoneVariant> mainStoneVariants;

    private List<Integer> ringSizes;

    private Boolean hasSideStones;

    private Integer sideStoneCount;

    private Double sideStoneSize;

    private String sideStoneShape;
}
