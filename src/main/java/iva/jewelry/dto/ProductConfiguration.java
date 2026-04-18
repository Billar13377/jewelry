package iva.jewelry.dto;

import lombok.Data;

@Data
public class ProductConfiguration {
    private String modelId;
    private String material;
    private Integer materialPurity;
    private String mainStone;
    private String mainStoneShape;
    private Double mainStoneSize;
    private String sideStone;
    private Integer ringSize;
}