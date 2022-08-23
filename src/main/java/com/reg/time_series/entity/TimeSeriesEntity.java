package com.reg.time_series.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeSeriesEntity {

    @Id
    @GeneratedValue
    private long id;

    private int version;

    private LocalDateTime timestamp;

    private String period;

    @ElementCollection
    private List<Integer> series;

}

