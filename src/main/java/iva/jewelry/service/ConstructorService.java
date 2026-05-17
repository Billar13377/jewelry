package iva.jewelry.service;

import iva.jewelry.dto.ProductConfiguration;
import iva.jewelry.dto.ProductSnapshot;
import iva.jewelry.model.Material;
import iva.jewelry.model.ProductLayer;
import iva.jewelry.model.ProductModel;
import iva.jewelry.model.Stone;
import iva.jewelry.repository.mongo.MaterialRepository;
import iva.jewelry.repository.mongo.ProductLayerRepository;
import iva.jewelry.repository.mongo.ProductModelRepository;
import iva.jewelry.repository.mongo.StoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConstructorService {

    private final MaterialRepository materialRepository;
    private final StoneRepository stoneRepository;
    private final ProductModelRepository productModelRepository;
    private final ProductLayerRepository productLayerRepository;

    private final PriceCalculationService priceService;
    private final SnapshotService snapshotService;

    public ProductSnapshot build(ProductConfiguration config) {
        ProductModel model = productModelRepository.findById(config.getModelId())
                .orElseThrow(() -> new RuntimeException("Model not found"));

        Material material = materialRepository
                .findByCode(config.getMaterial())
                .orElseThrow();

        Stone mainStone = null;

        if (model.getHasMainStone()) {
            mainStone = stoneRepository
                    .findByCode(config.getMainStone())
                    .orElseThrow(() -> new RuntimeException("Main Stone not found"));
        }

        Stone sideStone = null;

        if (config.getSideStone() != null) {
            sideStone = stoneRepository
                    .findByCode(config.getSideStone())
                    .orElseThrow();
        }

        BigDecimal price = priceService.calculate(
                config,
                model,
                material,
                mainStone,
                sideStone
        );

        return snapshotService.create(config, model, price);
    }

    public BigDecimal calculatePrice(ProductConfiguration config) {

        ProductModel model = productModelRepository.findById(config.getModelId())
                .orElseThrow(() -> new RuntimeException("Model not found"));
        Material material = materialRepository
                .findByCode(config.getMaterial())
                .orElseThrow(() -> new RuntimeException("Material not found"));

        Stone mainStone = null;

        if (config.getMainStone() != null && !config.getMainStone().isBlank()) {
            mainStone = stoneRepository
                    .findByCode(config.getMainStone())
                    .orElseThrow(() -> new RuntimeException("Main Stone not found"));
        }

        Stone sideStone = null;

        if (config.getSideStone() != null && !config.getSideStone().isBlank()) {
            sideStone = stoneRepository
                    .findByCode(config.getSideStone())
                    .orElseThrow(() -> new RuntimeException("Side stone not found"));
        }

        return priceService.calculate(
                config,
                model,
                material,
                mainStone,
                sideStone
        );
    }

    public List<ProductLayer> getLayers(String modelId){
        return productLayerRepository.findByModelId(modelId);
    }


}
