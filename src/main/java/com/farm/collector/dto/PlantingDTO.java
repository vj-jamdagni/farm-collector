package com.farm.collector.dto;

import com.farm.collector.enums.Season;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PlantingDTO {

    private Season season;
    private String farmName;
    private List<CropDTO> crops;
}
