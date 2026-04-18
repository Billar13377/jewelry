package iva.jewelry.service;

import iva.jewelry.model.*;
import iva.jewelry.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogService {
    private final CategoryRepository categoryRepository;
    private final ProductModelRepository modelRepository;
    private final ProductLayerRepository layerRepository;

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

}