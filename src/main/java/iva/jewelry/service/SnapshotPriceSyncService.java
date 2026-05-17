package iva.jewelry.service;

import iva.jewelry.dto.ProductConfiguration;
import iva.jewelry.dto.ProductSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SnapshotPriceSyncService {
    private final ConstructorService constructorService;

    public ProductSnapshot refreshPrice(ProductSnapshot snapshot) {
        ProductConfiguration config = new ProductConfiguration();
        config.setModelId(snapshot.getModelId());
        config.setMaterial(snapshot.getMaterial());
        config.setMaterialPurity(snapshot.getMaterialPurity());
        config.setMainStone(snapshot.getMainStone());
        config.setMainStoneShape(snapshot.getMainStoneShape());
        config.setMainStoneSize(snapshot.getMainStoneSize());
        config.setSideStone(snapshot.getSideStone());
        config.setRingSize(snapshot.getRingSize());

        snapshot.setPrice(constructorService.calculatePrice(config));
        return snapshot;
    }
}
