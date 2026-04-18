package iva.jewelry.controller;

import iva.jewelry.dto.*;
import iva.jewelry.model.ProductLayer;
import iva.jewelry.model.ProductModel;
import iva.jewelry.service.AdminProductService;
import iva.jewelry.service.CatalogService;
import iva.jewelry.service.ConstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
@RestController
@RequestMapping("/constructor")
@RequiredArgsConstructor
public class ConstructorController {
    private final ConstructorService constructor;
    private final AdminProductService service;


    @PostMapping("/build")
    public ProductSnapshot build(@RequestBody ProductConfiguration config) {
        return constructor.build(config);
    }
    @PostMapping("/price")
    public PriceResponse calculatePrice(
            @RequestBody ProductConfiguration config
    ) {

        BigDecimal price = constructor.calculatePrice(config);

        return new PriceResponse(price);
    }

    @PostMapping("/admin/models/{id}/stones")
    public ProductModel addStone(@PathVariable String id,
                                 @RequestBody StoneVariant variant) {
        return service.addStoneVariant(id, variant);
    }

    @DeleteMapping("/admin/models/{id}/stones")
    public ProductModel removeStone(@PathVariable String id,
                                    @RequestBody StoneVariant variant) {
        return service.removeStoneVariant(id, variant);
    }


    @PutMapping("/admin/models/{id}/materials/add")
    public ProductModel addMaterial(@PathVariable String id,
                                    @RequestParam String material) {
        return service.addMaterialToModel(id, material);
    }

    @PutMapping("/admin/models/{id}/materials/remove")
    public ProductModel removeMaterial(@PathVariable String id,
                                       @RequestParam String material) {
        return service.removeMaterialFromModel(id, material);
    }

    @PutMapping("/admin/models/{id}/sizes")
    public ProductModel updateSizes(@PathVariable String id,
                                    @RequestBody List<Integer> sizes) {
        return service.updateRingSizes(id, sizes);
    }

    @PutMapping("/admin/models/{id}/side-stones")
    public ProductModel updateSideStones(@PathVariable String id,
                                         @RequestParam Boolean has,
                                         @RequestParam Integer count,
                                         @RequestParam Double size,
                                         @RequestParam String shape) {
        return service.updateSideStones(id, has, count, size, shape);
    }

    @PostMapping("/admin/layers")
    public ProductLayer createLayer(@RequestBody ProductLayer layer) {
        return service.createLayer(layer);
    }
    @PutMapping("/admin/layers/{id}")
    public ProductLayer updateLayer(@PathVariable String id,
                                    @RequestBody ProductLayer productLayer) {
        return service.updateLayer(id, productLayer);
    }

    @GetMapping("/admin/layers")
    public List<ProductLayer> getLayers(@RequestParam(required = false) String modelId) {
        if (modelId == null || modelId.isEmpty()) {
            return service.getAllLayers();
        }
        return constructor.getLayers(modelId);
    }
    @DeleteMapping("/admin/layers/{id}")
    public void deleteLayer(@PathVariable String id) {
        service.deleteLayer(id);
    }
}
