package com.reg.time_series.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.TimeZone;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"power_station_id", "date"})
})
public class PowerStationDateData {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "power_station_id")
    private PowerStation powerStation;
    private LocalDate date;

    private TimeZone zone;

    @OneToOne (cascade = CascadeType.ALL)
    private TimeSeriesEntity latestTimeSeries;

    @OneToMany(cascade = CascadeType.ALL)
    private List<TimeSeriesEntity> previousTimeSeriesList;

}
