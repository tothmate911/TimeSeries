package com.reg.time_series.service;

import com.reg.time_series.entity.PowerStationDateData;
import com.reg.time_series.model.TimeSeriesInputModel;

import java.time.LocalDate;
import java.util.List;

public interface TimeSeriesService {

    PowerStationDateData processTimeSeries(TimeSeriesInputModel timeSeriesInputModel);

    List<String> getAllPowerStationNames();

    List<String> getAvailableDates(String powerStation);

    PowerStationDateData getPowerStationDateData(String powerStation, LocalDate date);
}
