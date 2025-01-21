package com.example.devicemanagement.dto;


import com.example.devicemanagement.entity.Device;

import java.util.UUID;

public class DeviceDTO {

    private UUID id_device;
    private String name_device;
    private String address;
    private int energy_consumption;
    private String description;

    public DeviceDTO() {

    }

    public DeviceDTO(UUID id_device, String name_device, String address, int energy_consumption, String description) {
        this.id_device = id_device;
        this.name_device = name_device;
        this.address = address;
        this.energy_consumption = energy_consumption;
        this.description = description;
    }

    public UUID getId_device() {
        return id_device;
    }

    public void setId_device(UUID id_device) {
        this.id_device = id_device;
    }

    public String getName_device() {
        return name_device;
    }

    public void setName_device(String name_device) {
        this.name_device = name_device;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getEnergy_consumption() {
        return energy_consumption;
    }

    public void setEnergy_consumption(int energy_consumption) {
        this.energy_consumption = energy_consumption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
