package iva.jewelry.dto;

import iva.jewelry.model.ProductLayer;
import iva.jewelry.model.ProductModel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ConstructorResponse {

    private ProductModel model;

    private List<ProductLayer> layers;

    private Map<String, List<Integer>> materialPurities;

}
