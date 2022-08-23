package com.reg.time_series.service;

import com.reg.time_series.entity.PowerStationDateData;
import com.reg.time_series.repository.PowerStationDayDataRepository;
import com.reg.time_series.entity.TimeSeriesEntity;
import com.reg.time_series.model.TimeSeriesInputModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

        TimeSeriesEntity newTimeSeries = TimeSeriesEntity.builder()
                .timestamp(timeSeriesInputModel.getTimestamp())
                .period(timeSeriesInputModel.getPeriod())
                .series(timeSeriesInputModel.getSeries())
                .build();

        if (powerStationDateData == null) {
            powerStationDateData = buildNewPowerStationDayData(timeSeriesInputModel, newTimeSeries);
        } else {
            updatePowerStationDayData(powerStationDateData, newTimeSeries);
        }

        return powerStationDateDataRepository.save(powerStationDateData);
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
        TimeSeriesEntity actualTimeSeriesEntity = powerStationDateData.getLatestTimeSeries();

        mergeSeries(actualTimeSeriesEntity, newTimeSeriesEntity);

        newTimeSeriesEntity.setVersion(actualTimeSeriesEntity.getVersion() + 1);
        powerStationDateData.getPreviousTimeSeriesList().add(actualTimeSeriesEntity);
        powerStationDateData.setLatestTimeSeries(newTimeSeriesEntity);
    }

    private void mergeSeries(TimeSeriesEntity actualTimeSeriesEntity, TimeSeriesEntity newTimeSeriesEntity) {
        List<Integer> newSeries = newTimeSeriesEntity.getSeries();
        List<Integer> actualSeries = actualTimeSeriesEntity.getSeries();
        if (newSeries.size() != actualSeries.size()) {
            throw new IllegalArgumentException(
                    "The sizes of the series in the new and the actual time series are different!\n" +
                            "actualTimeSeriesEntity: " + actualTimeSeriesEntity + "\n" +
                            "newTimeSeriesEntity: " + newTimeSeriesEntity);
        }

        // TODO check whether date and timestamp are on the same day
        int noChangeIndexLimit = calculateNoChangeIndexLimit(newTimeSeriesEntity);
        if (noChangeIndexLimit < newSeries.size()) {
            List<Integer> currentSeriesUntilNoChangeLimit = actualSeries
                    .subList(0, noChangeIndexLimit);
            List<Integer> newSeriesFromNoChangeLimit = newSeries.subList(noChangeIndexLimit, newSeries.size());
            newTimeSeriesEntity.setSeries(Stream.concat(
                    currentSeriesUntilNoChangeLimit.stream(),
                    newSeriesFromNoChangeLimit.stream()
            ).toList());
        } else {
            newTimeSeriesEntity.setSeries(actualSeries);
        }

    }

    private int calculateNoChangeIndexLimit(TimeSeriesEntity newTimeSeriesEntity) {
        LocalDateTime newTimestamp = newTimeSeriesEntity.getTimestamp(); // TODO timezone change from UTC to local

        // Minutes passed from start of the day until the end of actual timestamp + safety window
        int minutesUntilNoChangeLimit = newTimestamp.getHour() * 60 + newTimestamp.getMinute() + SAFETY_WINDOW_MINUTES;
        int period = parsePeriod(newTimeSeriesEntity.getPeriod());
        return (int) Math.ceil((double) minutesUntilNoChangeLimit / period);
    }

    private int parsePeriod(String periodString) {
        return Integer.parseInt(periodString.replaceAll("[^\\d.]", ""));
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
