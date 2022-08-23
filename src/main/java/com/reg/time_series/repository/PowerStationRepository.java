package com.reg.time_series.repository;

import com.reg.time_series.entity.PowerStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PowerStationRepository extends JpaRepository<PowerStation, Long> {

    @Query("SELECT p.name from PowerStation p")
    List<String> findAllPowerStationNames();

}
