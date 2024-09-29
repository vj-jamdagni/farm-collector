package com.farm.collector.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CropDTO {

    private String name;
    private double areaInAcre;
    private double expectedYield;
}
