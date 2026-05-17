package iva.jewelry.service;

import iva.jewelry.dto.MaterialVariant;
import iva.jewelry.dto.ProductConfiguration;
import iva.jewelry.model.Material;
import iva.jewelry.model.ProductModel;
import iva.jewelry.model.Stone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PriceCalculationService {
    public BigDecimal calculate(
            ProductConfiguration config,
            ProductModel model,
            Material material,
            Stone mainStone,
            Stone sideStone
    ){
        MaterialVariant variant = material.getVariants()
                .stream()
                .filter(v -> v.getPurity().equals(config.getMaterialPurity()))
                .findFirst()
                .orElseThrow();

        BigDecimal price = variant.getPricePerGram()
                .multiply(model.getWeight()).add(model.getBasePrice());

        if (mainStone != null && config.getMainStoneSize() != null) {
            price = price.add(
                    mainStone.getPricePerCarat()
                            .multiply(BigDecimal.valueOf(config.getMainStoneSize()))
            );
        }
        if (sideStone != null) {
            price = price.add(
                    sideStone.getPricePerCarat()
                            .multiply(BigDecimal.valueOf(model.getSideStoneSize()))
                            .multiply(BigDecimal.valueOf(model.getSideStoneCount()))
            );
        }
        return price;
    }
}