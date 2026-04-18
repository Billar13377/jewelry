package iva.jewelry.controller;

import iva.jewelry.model.Category;
import iva.jewelry.model.Material;
import iva.jewelry.model.ProductModel;
import iva.jewelry.model.Stone;
import iva.jewelry.service.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService service;

    @PostMapping("/categories")
    public Category createCategory(@RequestBody Category category) {
        return service.createCategory(category);
    }

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return service.getCategories();
    }

    @DeleteMapping("/categories/{id}")
    public void deleteCategory(@PathVariable String id) {
        service.deleteCategory(id);
    }

    @PostMapping("/models")
    public ProductModel createModel(@RequestBody ProductModel model) {
        return service.createModel(model);
    }

    @PutMapping("/models/{id}")
    public ProductModel updateModel(@PathVariable String id,
                                    @RequestBody ProductModel model) {
        return service.updateModel(id, model);
    }

    @DeleteMapping("/models/{id}")
    public void deleteModel(@PathVariable String id) {
        service.deleteModel(id);
    }

    @GetMapping("/models")
    public List<ProductModel> getModels() {
        return service.getAllModels();
    }

    @PostMapping("/materials")
    public Material createMaterial(@RequestBody Material material) {
        return service.createMaterial(material);
    }

    @GetMapping("/materials")
    public List<Material> getMaterials() {
        return service.getAllMaterials();
    }

    @PostMapping("/stones")
    public Stone createStone(@RequestBody Stone stone) {
        return service.createStone(stone);
    }

    @GetMapping("/stones")
    public List<Stone> getStones() {
        return service.getAllStones();
    }
}
