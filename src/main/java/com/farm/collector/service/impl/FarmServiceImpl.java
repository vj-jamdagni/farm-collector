package com.farm.collector.service.impl;

import com.farm.collector.dto.*;
import com.farm.collector.entity.Crop;
import com.farm.collector.entity.Farm;
import com.farm.collector.entity.Harvest;
import com.farm.collector.entity.Planting;
import com.farm.collector.enums.Season;
import com.farm.collector.exception.BaseFarmException;
import com.farm.collector.repo.CropRepository;
import com.farm.collector.repo.FarmRepository;
import com.farm.collector.repo.HarvestRepository;
import com.farm.collector.repo.PlantingRepository;
import com.farm.collector.service.FarmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FarmServiceImpl implements FarmService {

    @Autowired
    private HarvestRepository harvestRepository;

    @Autowired
    private PlantingRepository plantingRepository;

    @Autowired
    private FarmRepository farmRepository;

    @Autowired
    private CropRepository cropRepository;

    /**
     * Persists the plantation details of a given farm.
     *
     * @param plantingDTO : The plantation details as {@linkplain PlantingDTO}
     * @param farmId : The {@linkplain Farm} Identifier
     */
    @Override
    public void addPlantationDetails(PlantingDTO plantingDTO, Long farmId) {

        // If farm doesn't exist, create a new farm.
        if (farmRepository.findById(farmId).isEmpty()) {
            log.debug("Farm with id : {}, is not found. Creating a new farm.", farmId);
            farmRepository.save(Farm.builder()
                    .farmId(farmId)
                    .farmName(plantingDTO.getFarmName())
                    .build());
        }
        Farm farm = farmRepository.findById(farmId).get();

        for (CropDTO cropDTO : plantingDTO.getCrops()) {
            Crop crop = cropRepository.findByName(cropDTO.getName()) // add crop details if not exist
                    .orElseGet(() -> {
                        Crop newCrop = Crop.builder().name(cropDTO.getName()).build();
                        return cropRepository.save(newCrop);
                    });
            Planting planting = Planting.builder()
                    .season(plantingDTO.getSeason().name())
                    .area(cropDTO.getAreaInAcre())
                    .crop(crop)
                    .expectedYield(cropDTO.getExpectedYield())
                    .farm(farm)
                    .build();

            plantingRepository.save(planting);
        }

    }


    /**
     * Persists the Harvested details into the schema
     *
     * @param farmId : Farm Identifier
     * @param harvestDTO : Harvest Details
     */
    public void addHarvestDetails(Long farmId, HarvestDTO harvestDTO) {
        Farm farm = farmRepository.findById(farmId).orElseThrow(() -> new BaseFarmException("Farm not found"));

        for (CropHarvestDTO cropHarvestDTO : harvestDTO.getCrops()) {
            Crop crop = cropRepository.findByName(cropHarvestDTO.getName())
                    .orElseThrow(() -> new BaseFarmException("Crop not found"));

            Harvest harvest = Harvest.builder()
                    .actualYield(cropHarvestDTO.getActualYield())
                    .season(harvestDTO.getSeason().name())
                    .farm(farm)
                    .crop(crop)
                    .build();

            harvestRepository.save(harvest);
        }
    }

    /**
     * Generates a JSON report for the given season.
     *
     * @param season The {#{@linkplain Season}}
     * @return The {#{@linkplain ReportDTO}}
     */
    @Override
    public ReportDTO generateSeasonReport(Season season) {
        List<Planting> plantings = plantingRepository.findBySeason(season.name());
        List<Harvest> harvests = harvestRepository.findBySeason(season.name());

        Map<Long, FarmReportDTO> farmReports = new HashMap<>();

        for (Planting planting : plantings) {
            Farm farm = planting.getFarm();
            farmReports.putIfAbsent(farm.getFarmId(), FarmReportDTO.builder()
                    .crops(new ArrayList<>())
                    .farmName(farm.getFarmName()).build());
            FarmReportDTO farmReport = farmReports.get(farm.getFarmId());

            CropReportDTO cropReport = CropReportDTO.builder()
                    .name(planting.getCrop().getName())
                    .expectedYield(String.valueOf(planting.getExpectedYield()))
                    .actualYield("0") // Will be updated later.
                    .build();

            farmReport.getCrops().add(cropReport);
        }
        for (Harvest harvest : harvests) {
            Farm farm = harvest.getFarm();
            FarmReportDTO farmReport = farmReports.get(farm.getFarmId());

            if (farmReport != null) {
                for (CropReportDTO cropReport : farmReport.getCrops()) {
                    if (cropReport.getName().equals(harvest.getCrop().getName())) {
                        cropReport.setActualYield(String.valueOf(harvest.getActualYield()));
                        break;
                    }
                }
            }
        }

        ReportDTO reportDTO = ReportDTO.builder()
                .season(season)
                .reports(new ArrayList<>(farmReports.values()))
                .build();
        reportDTO.setSeason(season);
        reportDTO.setReports(new ArrayList<>(farmReports.values()));
        return reportDTO;
    }
}
