package com.farm.collector.controller;

import com.farm.collector.dto.HarvestDTO;
import com.farm.collector.dto.PlantingDTO;
import com.farm.collector.dto.ReportDTO;
import com.farm.collector.enums.Season;
import com.farm.collector.service.FarmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api")
@Slf4j
public class FarmController {

    @Autowired
    private FarmService farmService;

    @PostMapping("/farm/{farmId}/plantings")
    public ResponseEntity<String> plant(@RequestBody PlantingDTO plantingDTO, @PathVariable Long farmId) {
        log.debug("Request received for plantation details.");
        farmService.addPlantationDetails(plantingDTO, farmId);
        return ResponseEntity.ok("Plantation details successfully added.");
    }


    @PostMapping("/farm/{farmId}/harvests")
    public ResponseEntity<String> addHarvest(@PathVariable Long farmId, @RequestBody HarvestDTO harvestDTO) {
        farmService.addHarvestDetails(farmId, harvestDTO);
        return ResponseEntity.ok("Harvest data added.");
    }

    @GetMapping("/reports/seasons/{season}")
    public ResponseEntity<ReportDTO> getSeasonReport(@PathVariable String season) {
        ReportDTO report = farmService.generateSeasonReport(Season.valueOf(season.toUpperCase()));
        return ResponseEntity.ok(report);
    }
}
