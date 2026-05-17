package iva.jewelry.controller;

import iva.jewelry.dto.ConstructorResponse;
import iva.jewelry.dto.MaterialVariant;
import iva.jewelry.model.*;
import iva.jewelry.repository.mongo.MaterialRepository;
import iva.jewelry.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class CatalogController {
    private final CatalogService catalogService;

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
        Map<String, List<Integer>> purities = catalogService.getMaterialPurities(model);
        return new ConstructorResponse(model, layers, purities);
    }

}
