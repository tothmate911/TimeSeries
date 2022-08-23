package com.reg.time_series.controller;

import com.reg.time_series.entity.PowerStationDateData;
import com.reg.time_series.model.TimeSeriesInputModel;
import com.reg.time_series.service.TimeSeriesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api")
public class TimeSeriesController {

    @Autowired
    private TimeSeriesService timeSeriesService;

    @PutMapping("/time-series")
    public PowerStationDateData saveTimeSeries(@RequestBody TimeSeriesInputModel timeSeriesInputModel) {
        log.info("Processing " + timeSeriesInputModel.toString());
        return timeSeriesService.processTimeSeries(timeSeriesInputModel);
    }

    @GetMapping("/power-stations")
    public List<String> getPowerStations() {
        return timeSeriesService.getAllPowerStationNames();
    }

    @GetMapping("/available-dates/{powerStation}")
    public List<String> getAvailableDates(@PathVariable String powerStation) {
        return timeSeriesService.getAvailableDates(powerStation);
    }

    @GetMapping("/power-station-day-data")
    public PowerStationDateData getPowerStationDayData(@RequestParam("power-station") String powerStation,
                                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return timeSeriesService.getPowerStationDateData(powerStation, date);
    }

}
