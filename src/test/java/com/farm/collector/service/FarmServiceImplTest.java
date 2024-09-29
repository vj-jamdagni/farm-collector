package com.farm.collector.service;

import com.farm.collector.dto.*;
import com.farm.collector.entity.Crop;
import com.farm.collector.entity.Farm;
import com.farm.collector.entity.Harvest;
import com.farm.collector.entity.Planting;
import com.farm.collector.enums.Season;
import com.farm.collector.repo.CropRepository;
import com.farm.collector.repo.FarmRepository;
import com.farm.collector.repo.HarvestRepository;
import com.farm.collector.repo.PlantingRepository;
import com.farm.collector.service.impl.FarmServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class FarmServiceImplTest {

    @Mock
    private FarmRepository farmRepository;

    @Mock
    private CropRepository cropRepository;

    @Mock
    private PlantingRepository plantingRepository;

    @Mock
    private HarvestRepository harvestRepository;

    @InjectMocks
    private FarmServiceImpl farmService;


    @Test
    public void testAddPlantings() {
        // Arrange
        Long farmId = 1L;
        Farm farm = Farm.builder()
                .farmId(farmId)
                .farmName("MyFarm")
                .build();

        PlantingDTO plantingDTO = PlantingDTO.builder()
                .season(Season.SUMMER).build();

        List<CropDTO> cropDTOs = new ArrayList<>();
        CropDTO crop1 = CropDTO.builder()
                .name("corn")
                .areaInAcre(10.5)
                .expectedYield(20.5)
                .build();
        cropDTOs.add(crop1);

        plantingDTO.setCrops(cropDTOs);

        Crop crop = Crop.builder().name("corn").build();

        // Mock behavior
        Mockito.when(farmRepository.findById(farmId)).thenReturn(Optional.of(farm));
        Mockito.when(cropRepository.findByName("corn")).thenReturn(Optional.of(crop));

        // Act
        farmService.addPlantationDetails(plantingDTO, farmId);

        // Assert
        Mockito.verify(plantingRepository, Mockito.times(1)).save(Mockito.any(Planting.class));
    }

    @Test
    public void testAddHarvest() {
        // Arrange
        Long farmId = 1L;
        Farm farm = Farm.builder()
                .farmName("MyFarm")
                .farmId(farmId)
                .build();

        HarvestDTO harvestDTO = HarvestDTO.builder()
                .season(Season.SPRING).build();

        List<CropHarvestDTO> cropHarvestDTOs = new ArrayList<>();
        CropHarvestDTO crop1 = CropHarvestDTO.builder()
                .name("corn")
                .actualYield(18.0)
                .build();
        cropHarvestDTOs.add(crop1);

        harvestDTO.setCrops(cropHarvestDTOs);

        Crop crop = Crop.builder().name("corn").build();

        // Mock behavior
        Mockito.when(farmRepository.findById(farmId)).thenReturn(Optional.of(farm));
        Mockito.when(cropRepository.findByName("corn")).thenReturn(Optional.of(crop));

        // Act
        farmService.addHarvestDetails(farmId, harvestDTO);

        // Assert
        Mockito.verify(harvestRepository, Mockito.times(1)).save(Mockito.any(Harvest.class));
    }


    @Test
    public void testGenerateSeasonReport() {
        // Arrange
        Farm farm = Farm.builder()
                .farmId(1L)
                .farmName("MyFarm").build();

        Crop crop1 = Crop.builder()
                .name("corn").build();

        Crop crop2 = Crop.builder()
                .name("potatoes").build();

        Planting planting1 = Planting.builder()
                .farm(farm)
                .crop(crop1)
                .expectedYield(20.0)
                .season(Season.SPRING.name())
                .build();

        Planting planting2 = Planting.builder()
                .farm(farm)
                .crop(crop2)
                .expectedYield(15.0)
                .season(Season.SPRING.name())
                .build();

        Harvest harvest1 = Harvest.builder()
                .farm(farm)
                .crop(crop1)
                .actualYield(18.0)
                .season(Season.SPRING.name())
                .build();

        Harvest harvest2 = Harvest.builder()
                .farm(farm)
                .crop(crop2)
                .actualYield(12.0)
                .season(Season.SPRING.name())
                .build();

        List<Planting> plantings = Arrays.asList(planting1, planting2);
        List<Harvest> harvests = Arrays.asList(harvest1, harvest2);

        // Mock behavior
        Mockito.when(plantingRepository.findBySeason(Season.SPRING.name())).thenReturn(plantings);
        Mockito.when(harvestRepository.findBySeason(Season.SPRING.name())).thenReturn(harvests);

        // Act
        ReportDTO reportDTO = farmService.generateSeasonReport(Season.SPRING);

        // Assert
        assertEquals(Season.SPRING, reportDTO.getSeason());
        assertEquals(1, reportDTO.getReports().size());

        FarmReportDTO farmReport = reportDTO.getReports().get(0);
        assertEquals("MyFarm", farmReport.getFarmName());
        assertEquals(2, farmReport.getCrops().size());

        CropReportDTO cropReport1 = farmReport.getCrops().stream()
                .filter(cropReport -> cropReport.getName().equals("corn"))
                .findFirst()
                .orElse(null);

        CropReportDTO cropReport2 = farmReport.getCrops().stream()
                .filter(cropReport -> cropReport.getName().equals("potatoes"))
                .findFirst()
                .orElse(null);

        assertNotNull(cropReport1);
        assertNotNull(cropReport2);

        assertEquals(20.0, Double.valueOf(cropReport1.getExpectedYield()));
        assertEquals(18.0, Double.valueOf(cropReport1.getActualYield()));

        assertEquals(15.0, Double.valueOf(cropReport2.getExpectedYield()));
        assertEquals(12.0, Double.valueOf(cropReport2.getActualYield()));
    }

}
