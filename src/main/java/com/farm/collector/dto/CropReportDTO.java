package com.farm.collector.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CropReportDTO {
    private String name;
    private String expectedYield;
    private String actualYield;
}
