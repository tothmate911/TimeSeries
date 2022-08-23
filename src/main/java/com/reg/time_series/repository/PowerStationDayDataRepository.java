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

    @Query("SELECT p FROM PowerStationDateData p WHERE p.powerStation.name = :powerStationName AND date = :date")
    PowerStationDateData findByPowerStationNameAndDate(@Param("powerStationName") String powerStationName,
                                                       @Param("date") LocalDate date);

    @Query("SELECT p.date FROM PowerStationDateData p WHERE p.powerStation.name = :powerStationName")
    List<LocalDate> findAvailableDatesByPowerStationName(@Param("powerStationName") String powerStationName);

}
