package com.farm.collector.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CropHarvestDTO {

    private String name;
    private double actualYield;
}