package com.reg.time_series.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeSeriesInputModel {

    @JsonProperty("power-station")
    private String powerStation;

    private LocalDate date;
    private TimeZone zone;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private String period;

    private List<Integer> series;

}
