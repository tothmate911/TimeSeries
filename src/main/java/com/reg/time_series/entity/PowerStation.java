package com.reg.time_series.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class PowerStation {

    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private String name;

    public PowerStation(String name) {
        this.name = name;
    }

}
