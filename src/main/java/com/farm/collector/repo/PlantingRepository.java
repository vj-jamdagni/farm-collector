package com.farm.collector.repo;

import com.farm.collector.entity.Planting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantingRepository extends JpaRepository<Planting, Long> {

    List<Planting> findBySeason(String season);
}
