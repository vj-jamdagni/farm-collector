package com.farm.collector.service;

import com.farm.collector.dto.HarvestDTO;
import com.farm.collector.dto.PlantingDTO;
import com.farm.collector.dto.ReportDTO;
import com.farm.collector.enums.Season;

public interface FarmService {
    void addPlantationDetails(PlantingDTO plantingDTO, Long farmId);

    void addHarvestDetails(Long farmId, HarvestDTO harvestDTO);

    ReportDTO generateSeasonReport(Season season);
}
