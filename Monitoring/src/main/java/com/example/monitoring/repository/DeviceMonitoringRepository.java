package com.example.monitoring.repository;

import com.example.monitoring.entity.DeviceMonitoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeviceMonitoringRepository extends JpaRepository<DeviceMonitoring, UUID> {

    DeviceMonitoring findDeviceMonitoringByDeviceId(UUID device_id);
}
