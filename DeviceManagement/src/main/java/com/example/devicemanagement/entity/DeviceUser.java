package com.example.devicemanagement.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "device_user")
public class DeviceUser {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id_commontable")
    private UUID id_commontable;

    @ManyToOne
    @JoinColumn(name = "id_device")
    private Device device;

    @Column(name = "id_user")
    private UUID idUser;


    public DeviceUser() {

    }

    public DeviceUser(UUID id_commontable, Device device, UUID idUser) {
        this.id_commontable = id_commontable;
        this.device = device;
        this.idUser = idUser;
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
        return idUser;
    }

    public void setId_user(UUID idUser) {
        this.idUser = idUser;
    }

}
