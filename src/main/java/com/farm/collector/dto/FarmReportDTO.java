package com.farm.collector.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FarmReportDTO {

    private String farmName;
    private List<CropReportDTO> crops;
}
