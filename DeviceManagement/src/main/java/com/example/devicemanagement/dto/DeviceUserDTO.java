package com.example.devicemanagement.dto;

import com.example.devicemanagement.entity.Device;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

public class DeviceUserDTO {

    private UUID id_commontable;
    private Device device;
    private UUID id_user;


    public DeviceUserDTO() {

    }

    public DeviceUserDTO(UUID id_commontable, Device device, UUID id_user) {
        this.id_commontable = id_commontable;
        this.device = device;
        this.id_user = id_user;
    }

    public DeviceUserDTO(Device device, UUID id_user, int energyConsumption) {
        this.device = device;
        this.id_user = id_user;
    }

    public UUID getId_commontable() {
        return id_commontable;
    }

    public void setId_commontable(UUID id_commontable) {
        this.id_commontable = id_commontable;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public UUID getId_user() {
        return id_user;
    }

    public void setId_user(UUID id_user) {
        this.id_user = id_user;
    }

}
