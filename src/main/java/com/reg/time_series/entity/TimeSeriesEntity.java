package com.reg.time_series.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.TimeZone;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class TimeSeriesEntity {

    @Id
    @GeneratedValue
    private long id;

    private int version;

    // This is always to be interpreted in UTC
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private String period;

    @ElementCollection
    private List<Integer> series;

    private int noChangeIndexLimit;

    public void calculateNoChangeIndexLimit(TimeZone zone, LocalDate date, int safetyWindowInMinutes) {
        ZoneId zoneId = zone.toZoneId();
        ZonedDateTime newSeriesEndOfSafetyWindow = timestamp.atZone(ZoneId.of("UTC")).plusMinutes(safetyWindowInMinutes);
        ZonedDateTime startOfTheDate = date.atStartOfDay().atZone(zoneId);

        // Minutes from the start of the date until the timeStamp + safety window
        long minutesUntilNoChangeLimit = ChronoUnit.MINUTES.between(startOfTheDate, newSeriesEndOfSafetyWindow);
        int periodInt = Integer.parseInt(period.replaceAll("[^\\d.]", ""));

        noChangeIndexLimit = (int) Math.ceil((double) minutesUntilNoChangeLimit / periodInt);
    }

}

