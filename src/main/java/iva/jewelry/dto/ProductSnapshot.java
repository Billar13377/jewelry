package iva.jewelry.dto;

import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;


@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Jacksonized
public class ProductSnapshot {
    private String modelId;
    private String modelName;
    private String category;
    private String material;
    private Integer materialPurity;
    private BigDecimal weight;
    private String mainStone;

    private String mainStoneShape;

    private Double mainStoneSize;

    private String sideStone;

    private Integer sideStoneCount;

    private Double sideStoneSize;
    private String sideStoneShape;
    private Integer ringSize;
    private BigDecimal price;

    public ProductSnapshot normalize() {
        return ProductSnapshot.builder()
                .modelName(trim(modelName))
                .category(trim(category))
                .material(trim(material))
                .materialPurity(materialPurity)
                .weight(normalizeBigDecimal(weight))
                .mainStone(trim(mainStone))
                .mainStoneShape(trim(mainStoneShape))
                .mainStoneSize(normalizeDouble(mainStoneSize))
                .sideStone(trim(sideStone))
                .sideStoneCount(sideStoneCount)
                .sideStoneSize(normalizeDouble(sideStoneSize))
                .sideStoneShape(trim(sideStoneShape))
                .ringSize(ringSize)
                .build();
    }
    private String trim(String s) {
        return s == null ? null : s.trim();
    }

    private BigDecimal normalizeBigDecimal(BigDecimal b) {
        return b == null ? null : b.stripTrailingZeros();
    }

    private Double normalizeDouble(Double d) {
        return d == null ? null : Math.round(d * 1000.0) / 1000.0;
    }
}