package com.example.monitoring.service;

import com.example.monitoring.entity.DeviceMonitoring;
import com.example.monitoring.repository.DeviceMonitoringRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeviceMonitoringService {

    private final DeviceMonitoringRepository deviceMonitoringRepository;

    public DeviceMonitoringService(DeviceMonitoringRepository deviceMonitoringRepository) {
        this.deviceMonitoringRepository = deviceMonitoringRepository;
    }

    public void addDeviceMonitoring(DeviceMonitoring deviceMonitoring) {
        deviceMonitoringRepository.save(deviceMonitoring);
    }

    public void deleteDeviceMonitoring(UUID id) {
        try {
            deviceMonitoringRepository.deleteById(id);
            System.out.println("Deleted succesfull");
        } catch (Exception e) {
            System.out.println("Cannot delete");
        }

    }

    public DeviceMonitoring findByDeviceId(UUID device_id) {
        return deviceMonitoringRepository.findDeviceMonitoringByDeviceId(device_id);
    }
}
