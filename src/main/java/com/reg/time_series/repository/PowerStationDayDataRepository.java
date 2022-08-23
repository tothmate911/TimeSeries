package com.reg.time_series.repository;

import com.reg.time_series.entity.PowerStationDateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PowerStationDayDataRepository extends JpaRepository<PowerStationDateData, Long> {

    PowerStationDateData findByPowerStationAndDate(String powerStationName, LocalDate date);

    @Query("SELECT p.date FROM PowerStationDateData p WHERE p.powerStation = :powerStation")
    List<LocalDate> findAvailableDatesByPowerStation(@Param("powerStation") String powerStation);

    @Query("SELECT DISTINCT p.powerStation FROM PowerStationDateData p")
    List<String> findAllPowerStations();
}
