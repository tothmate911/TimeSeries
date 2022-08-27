package com.reg.time_series.service;

import com.reg.time_series.entity.PowerStationDateData;
import com.reg.time_series.repository.PowerStationDayDataRepository;
import com.reg.time_series.entity.TimeSeriesEntity;
import com.reg.time_series.model.TimeSeriesInputModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
public class TimeSeriesServiceImpl implements TimeSeriesService {
    private static final int SAFETY_WINDOW_MINUTES = 90;

    @Autowired
    private PowerStationDayDataRepository powerStationDateDataRepository;

    @Override
    public PowerStationDateData processTimeSeries(TimeSeriesInputModel timeSeriesInputModel) {
        PowerStationDateData powerStationDateData = powerStationDateDataRepository.findByPowerStationAndDate(
                timeSeriesInputModel.getPowerStation(), timeSeriesInputModel.getDate());
        TimeSeriesEntity newTimeSeriesEntity = buildTimeTimeSeriesEntityFromModel(timeSeriesInputModel);

        if (powerStationDateData == null) {
            powerStationDateData = buildNewPowerStationDayData(timeSeriesInputModel, newTimeSeriesEntity);
        } else {
            updatePowerStationDayData(powerStationDateData, newTimeSeriesEntity);
        }
        return powerStationDateDataRepository.save(powerStationDateData);
    }

    private TimeSeriesEntity buildTimeTimeSeriesEntityFromModel(TimeSeriesInputModel timeSeriesInputModel) {
        TimeSeriesEntity newTimeSeries = TimeSeriesEntity.builder()
                .timestamp(timeSeriesInputModel.getTimestamp())
                .period(timeSeriesInputModel.getPeriod())
                .series(timeSeriesInputModel.getSeries())
                .build();
        newTimeSeries.calculateNoChangeIndexLimit(
                timeSeriesInputModel.getZone(), timeSeriesInputModel.getDate(), SAFETY_WINDOW_MINUTES);
        return newTimeSeries;
    }

    private PowerStationDateData buildNewPowerStationDayData(TimeSeriesInputModel timeSeriesInputModel,
                                                             TimeSeriesEntity newTimeSeries) {
        // The version of the time series is 1 because it is the first time series
        newTimeSeries.setVersion(1);
        return PowerStationDateData.builder()
                .powerStation(timeSeriesInputModel.getPowerStation())
                .date(timeSeriesInputModel.getDate())
                .zone(timeSeriesInputModel.getZone())
                .latestTimeSeries(newTimeSeries)
                .build();
    }

    private void updatePowerStationDayData(PowerStationDateData powerStationDateData, TimeSeriesEntity newTimeSeriesEntity) {
        TimeSeriesEntity latestSavedTimeSeriesEntity = powerStationDateData.getLatestTimeSeries();
        // Check if latest saved and new series size are equal
        checkSeriesSize(newTimeSeriesEntity, latestSavedTimeSeriesEntity);
        // Merge the latest saved and the new series
        newTimeSeriesEntity.setSeries(mergeSeries(
                latestSavedTimeSeriesEntity.getSeries(),
                newTimeSeriesEntity.getSeries(), newTimeSeriesEntity.getNoChangeIndexLimit())
        );
        // Add the new TimeSeries as the latest saved TimeSeries
        newTimeSeriesEntity.setVersion(latestSavedTimeSeriesEntity.getVersion() + 1);
        powerStationDateData.setLatestTimeSeries(newTimeSeriesEntity);
        powerStationDateData.getPreviousTimeSeriesList().add(latestSavedTimeSeriesEntity);
    }

    private void checkSeriesSize(TimeSeriesEntity newTimeSeriesEntity, TimeSeriesEntity latestSavedTimeSeriesEntity) {
        if (newTimeSeriesEntity.getSeries().size() != latestSavedTimeSeriesEntity.getSeries().size()) {
            throw new IllegalArgumentException(
                    "The sizes of the series in the new and the actual time series are different!\n" +
                            "actualTimeSeriesEntity: " + latestSavedTimeSeriesEntity + "\n" +
                            "newTimeSeriesEntity: " + newTimeSeriesEntity);
        }
    }

    private List<Integer> mergeSeries(List<Integer> latestSavedSeries, List<Integer> newSeries, int noChangeIndexLimit) {
        // In this case the timestamp + safety window is after the end of the particular day
        if (noChangeIndexLimit >= newSeries.size()) {
            log.warn("The no change limit calculated from timestamp and safety window is beyond the whole series, " +
                    "series remain the same");
            return latestSavedSeries;
        }

        // Concatenate elements from the latest saved series up to the noChangeIndexLimit, and form that on from the new series
        List<Integer> currentSeriesUntilNoChangeLimit = latestSavedSeries
                .subList(0, noChangeIndexLimit);
        List<Integer> newSeriesFromNoChangeLimit = newSeries.subList(noChangeIndexLimit, newSeries.size());
        return Stream.concat(
                currentSeriesUntilNoChangeLimit.stream(),
                newSeriesFromNoChangeLimit.stream()
        ).toList();
    }

    @Override
    public List<String> getAllPowerStationNames() {
        return powerStationDateDataRepository.findAllPowerStations();
    }

    @Override
    public List<String> getAvailableDates(String powerStationName) {
        return powerStationDateDataRepository.findAvailableDatesByPowerStation(powerStationName).stream()
                .map(LocalDate::toString).toList();
    }

    @Override
    public PowerStationDateData getPowerStationDateData(String powerStation, LocalDate date) {
        return powerStationDateDataRepository.findByPowerStationAndDate(powerStation, date);
    }

}
