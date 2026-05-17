package iva.jewelry.service;

import iva.jewelry.dto.StoneVariant;
import iva.jewelry.model.*;
import iva.jewelry.repository.mongo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final CategoryRepository categoryRepository;
    private final ProductModelRepository modelRepository;
    private final MaterialRepository materialRepository;
    private final StoneRepository stoneRepository;
    private final ProductLayerRepository productLayerRepository;

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public void deleteCategory(String id) {
        categoryRepository.deleteById(id);
    }

    public ProductModel createModel(ProductModel model) {
        return modelRepository.save(model);
    }

    public ProductModel updateModel(String id, ProductModel updated) {
        ProductModel model = modelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Model not found"));

        model.setName(updated.getName());
        model.setBasePrice(updated.getBasePrice());
        model.setMaterials(updated.getMaterials());
        model.setWeight(updated.getWeight());
        model.setMainStoneVariants(updated.getMainStoneVariants());
        model.setRingSizes(updated.getRingSizes());
        model.setHasSideStones(updated.getHasSideStones());
        model.setSideStoneCount(updated.getSideStoneCount());
        model.setSideStoneSize(updated.getSideStoneSize());
        model.setSideStoneShape(updated.getSideStoneShape());

        return modelRepository.save(model);
    }

    public void deleteModel(String id) {
        modelRepository.deleteById(id);
    }

    public List<ProductModel> getAllModels() {
        return modelRepository.findAll();
    }

    public List<Material> getAllMaterials(){
        return materialRepository.findAll();
    }
    public List<Stone> getAllStones() {
        return stoneRepository.findAll();
    }

    public Material createMaterial(Material material) {
        return materialRepository.save(material);
    }
    public Stone createStone(Stone stone) {
        return stoneRepository.save(stone);
    }
    @Transactional
    public ProductModel addStoneVariant(String modelId, StoneVariant variant) {

        ProductModel model = modelRepository.findById(modelId)
                .orElseThrow(() -> new RuntimeException("Model not found"));

        boolean exists = model.getMainStoneVariants().stream().anyMatch(v ->
                v.getStone().equals(variant.getStone()) &&
                        v.getShape().equals(variant.getShape()) &&
                        v.getSize().equals(variant.getSize())
        );

        if (exists) {
            throw new RuntimeException("Такой вариант уже существует");
        }

        model.getMainStoneVariants().add(variant);

        return modelRepository.save(model);
    }
    @Transactional
    public ProductModel removeStoneVariant(String modelId, StoneVariant variant) {

        ProductModel model = modelRepository.findById(modelId)
                .orElseThrow(() -> new RuntimeException("Model not found"));

        model.setMainStoneVariants(
                model.getMainStoneVariants().stream()
                        .filter(v -> !(v.getStone().equals(variant.getStone())
                                && v.getShape().equals(variant.getShape())
                                && v.getSize().equals(variant.getSize())))
                        .toList()
        );

        return modelRepository.save(model);
    }
    @Transactional
    public ProductModel addMaterialToModel(String modelId, String materialCode) {

        ProductModel model = modelRepository.findById(modelId)
                .orElseThrow();

        if (!model.getMaterials().contains(materialCode)) {
            model.getMaterials().add(materialCode);
        }

        return modelRepository.save(model);
    }
    @Transactional
    public ProductModel removeMaterialFromModel(String modelId, String materialCode) {

        ProductModel model = modelRepository.findById(modelId)
                .orElseThrow();

        model.getMaterials().remove(materialCode);

        return modelRepository.save(model);
    }
    @Transactional
    public ProductModel updateRingSizes(String modelId, List<Integer> sizes) {

        ProductModel model = modelRepository.findById(modelId)
                .orElseThrow();

        model.setRingSizes(sizes);

        return modelRepository.save(model);
    }
    @Transactional
    public ProductModel updateSideStones(String modelId,
                                         Boolean hasSideStones,
                                         Integer count,
                                         Double size,
                                         String shape) {

        ProductModel model = modelRepository.findById(modelId)
                .orElseThrow();

        model.setHasSideStones(hasSideStones);
        model.setSideStoneCount(count);
        model.setSideStoneSize(size);
        model.setSideStoneShape(shape);

        return modelRepository.save(model);
    }
    public ProductLayer createLayer(ProductLayer layer) {
        return productLayerRepository.save(layer);
    }

    public void deleteLayer(String id) {
        productLayerRepository.deleteById(id);
    }

    public List<ProductLayer> getAllLayers() {
        return productLayerRepository.findAll();
    }

    @Transactional
    public ProductLayer updateLayer(String id, ProductLayer updated) {
        ProductLayer productLayer = productLayerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Layer not found"));
        productLayer.setModelId(updated.getModelId());
        productLayer.setType(updated.getType());
        productLayer.setMaterial(updated.getMaterial());
        productLayer.setStone(updated.getStone());
        productLayer.setShape(updated.getShape());
        productLayer.setSize(updated.getSize());
        productLayer.setImageUrl(updated.getImageUrl());

        return productLayerRepository.save(productLayer);
    }
}
