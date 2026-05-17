package iva.jewelry.service;

import iva.jewelry.dto.MaterialVariant;
import iva.jewelry.model.*;
import iva.jewelry.repository.mongo.CategoryRepository;
import iva.jewelry.repository.mongo.MaterialRepository;
import iva.jewelry.repository.mongo.ProductLayerRepository;
import iva.jewelry.repository.mongo.ProductModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogService {
    private final CategoryRepository categoryRepository;
    private final ProductModelRepository modelRepository;
    private final ProductLayerRepository layerRepository;
    private final MaterialRepository materialRepository;

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public List<ProductModel> getModelsByCategory(String categoryId) {
        return modelRepository.findByCategoryId(categoryId);
    }

    public ProductModel getModel(String modelId) {
        return modelRepository.findById(modelId)
                .orElseThrow(() -> new RuntimeException("Model not found"));
    }

        public List<ProductLayer> getLayers(String modelId) {
        return layerRepository.findByModelId(modelId);
    }

    public Map<String, List<Integer>> getMaterialPurities(ProductModel model) {
        return model.getMaterials().stream()
                .collect(Collectors.toMap(
                        code -> code,
                        code -> materialRepository.findByCode(code)
                                .orElseThrow(() -> new RuntimeException("Material not found: " + code))
                                .getVariants().stream()
                                .map(MaterialVariant::getPurity)
                                .toList()
                ));
    }
}