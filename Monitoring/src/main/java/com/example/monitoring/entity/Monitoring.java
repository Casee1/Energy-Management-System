package com.example.monitoring.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "monitoring")
public class Monitoring {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "timestamp")
    private String timestamp;

    @Column(name = "measurement")
    private float measurement;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private DeviceMonitoring device_id;

    public Monitoring() {

    }

    public Monitoring(int id, String timestamp, float measurement, DeviceMonitoring device_id) {
        this.id = id;
        this.timestamp = timestamp;
        this.measurement = measurement;
        this.device_id = device_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public float getMeasurement() {
        return measurement;
    }

    public void setMeasurement(float measurement) {
        this.measurement = measurement;
    }

    public DeviceMonitoring getDevice_id() {
        return device_id;
    }

    public void setDevice_id(DeviceMonitoring device_id) {
        this.device_id = device_id;
    }
}
