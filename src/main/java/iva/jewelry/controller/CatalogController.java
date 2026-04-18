package iva.jewelry.controller;

import iva.jewelry.dto.ConstructorResponse;
import iva.jewelry.dto.MaterialVariant;
import iva.jewelry.model.*;
import iva.jewelry.repository.MaterialRepository;
import iva.jewelry.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class CatalogController {
    private final CatalogService catalogService;
    private final MaterialRepository materialRepository;

    @GetMapping("/categories")
    public List<Category> categories() { return catalogService.getCategories(); }

    @GetMapping("/categories/{categoryId}/models")
    public List<ProductModel> getModelsByCategory(
            @PathVariable String categoryId
    ) {
        return catalogService.getModelsByCategory(categoryId);
    }

    @GetMapping("/models/{modelId}")
    public ConstructorResponse getModelConstructor(@PathVariable String modelId) {

        ProductModel model = catalogService.getModel(modelId);
        List<ProductLayer> layers = catalogService.getLayers(modelId);
        Map<String, List<Integer>> purities = buildMaterialPurities(model);


        return new ConstructorResponse(model, layers, purities);
    }

    private Map<String, List<Integer>> buildMaterialPurities(ProductModel model) {

        Map<String, List<Integer>> result = new HashMap<>();

        for (String materialCode : model.getMaterials()) {

            Material material = materialRepository
                    .findByCode(materialCode)
                    //.findById(materialCode)
                    .orElseThrow(() -> new RuntimeException("Material not found " + materialCode));

            List<Integer> purities = material.getVariants()
                    .stream()
                    .map(MaterialVariant::getPurity)
                    .toList();

            result.put(materialCode, purities);
        }

        return result;
    }
}
