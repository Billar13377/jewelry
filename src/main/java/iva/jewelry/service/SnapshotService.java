package iva.jewelry.service;

import iva.jewelry.dto.ProductConfiguration;
import iva.jewelry.dto.ProductSnapshot;
import iva.jewelry.model.ProductModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SnapshotService {

    public ProductSnapshot create(
            ProductConfiguration config,
            ProductModel model,
            BigDecimal price
    ) {
        return ProductSnapshot.builder()
                .modelId(model.getId())
                .modelName(model.getName())
                .category(model.getCategoryId())
                .material(config.getMaterial())
                .weight(model.getWeight())
                .materialPurity(config.getMaterialPurity())
                .mainStone(config.getMainStone())
                .mainStoneShape(config.getMainStoneShape())
                .mainStoneSize(config.getMainStoneSize())
                .sideStone(config.getSideStone())
                .sideStoneCount(model.getSideStoneCount())
                .sideStoneSize(model.getSideStoneSize())
                .sideStoneShape(model.getSideStoneShape())
                .ringSize(config.getRingSize())
                .price(price)
                .build();
    }
}