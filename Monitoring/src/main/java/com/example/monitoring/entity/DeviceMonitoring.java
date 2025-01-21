package com.example.monitoring.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "device_monitoring")
public class DeviceMonitoring {

    @Id
    @Column(name = "device_id")
    private UUID deviceId;

    @Column(name = "max_energy_consumption")
    private int max_energy_consumption;

    @Column(name = "user_id")
    private UUID user_id;

    public DeviceMonitoring() {

    }

    public DeviceMonitoring(UUID deviceId, int max_energy_consumption, UUID user_id) {
        this.deviceId = deviceId;
        this.max_energy_consumption = max_energy_consumption;
        this.user_id = user_id;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public int getMax_energy_consumption() {
        return max_energy_consumption;
    }

    public void setMax_energy_consumption(int max_energy_consumption) {
        this.max_energy_consumption = max_energy_consumption;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }
}
