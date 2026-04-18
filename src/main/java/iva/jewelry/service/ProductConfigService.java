package iva.jewelry.service;

import iva.jewelry.dto.ProductConfiguration;

import iva.jewelry.model.Material;
import iva.jewelry.model.ProductModel;
import iva.jewelry.repository.MaterialRepository;
import iva.jewelry.repository.ProductModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductConfigService {

    private final ProductModelRepository modelRepository;
    private final MaterialRepository materialRepository;

    public ProductModel validate(ProductConfiguration config) {

        ProductModel model = modelRepository.findById(config.getModelId())
                .orElseThrow(() -> new RuntimeException("Model not found"));

        validateMaterial(model, config.getMaterial());
        Material material = materialRepository.findByCode(config.getMaterial())
                .orElseThrow(() -> new RuntimeException("Material not found"));

        validateMaterialPurity(material, config.getMaterialPurity());

        validateStoneVariant(model, config);
        validateRingSize(model, config.getRingSize());
        validateSideStone(model, config.getSideStone());

        return model;
    }

    private void validateMaterial(ProductModel model, String material) {

        if (!model.getMaterials().contains(material)) {
            throw new RuntimeException("Material not allowed for this model " + material + model.getMaterials());
        }
    }
    private void validateMaterialPurity(Material material, Integer purity) {

        boolean allowed = material.getVariants()
                .stream()
                .anyMatch(v -> v.getPurity().equals(purity));

        if (!allowed) {
            throw new RuntimeException("Purity not allowed");
        }
    }

//    private void validateStoneVariant(ProductModel model, ProductConfiguration config) {
//        boolean allowed = model.getMainStoneVariants()
//                .stream()
//                .anyMatch(v ->
//                        v.getStone().equals(config.getMainStone()) &&
//                                v.getShape().equals(config.getMainStoneShape()) &&
//                                Double.compare(v.getSize(), config.getMainStoneSize()) == 0
//                                //v.getSize().equals(config.getMainStoneSize())
//                );
//
//        if(!allowed){
//            throw new RuntimeException("Stone variant not allowed");
//        }
//    }
private void validateStoneVariant(ProductModel model, ProductConfiguration config) {

    if (!model.getHasMainStone()) {
        if (config.getMainStone() != null) {
            throw new RuntimeException("Main stone not supported for this model");
        }
        return;
    }
    if (config.getMainStone() == null ||
            config.getMainStoneShape() == null ||
            config.getMainStoneSize() == null) {
        throw new RuntimeException("Main stone configuration required");
    }
    boolean allowed = model.getMainStoneVariants()
            .stream()
            .anyMatch(v ->
                    v.getStone().equalsIgnoreCase(config.getMainStone()) &&
                            v.getShape().equalsIgnoreCase(config.getMainStoneShape()) &&
                            Double.compare(v.getSize(), config.getMainStoneSize()) == 0
            );
    if (!allowed) {
        throw new RuntimeException("Stone variant not allowed");
    }
}

    private void validateRingSize(ProductModel model, Integer ringSize) {
        if (model.getRingSizes() == null || model.getRingSizes().isEmpty()) {
            return;
        }

        if (ringSize == null || !model.getRingSizes().contains(ringSize)) {
            throw new RuntimeException("Ring size not allowed");
        }
    }

    private void validateSideStone(ProductModel model, String sideStone) {
        if (!model.getHasSideStones()) {
            if (sideStone != null) {
                throw new RuntimeException("Side stones not supported for this model");
            }
            return;
        }
        if (sideStone == null) {
            throw new RuntimeException("Side stone required");
        }
    }
}