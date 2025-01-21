package com.example.monitoring.service;


import com.example.monitoring.entity.Monitoring;
import com.example.monitoring.repository.MonitoringRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class MonitoringService {

    private final MonitoringRepository monitoringRepository;

    @Autowired
    public MonitoringService(MonitoringRepository monitoringRepository) {
        this.monitoringRepository = monitoringRepository;
    }
    public void addMonitoring(Monitoring mon){
        monitoringRepository.save(mon);
    }


    public List<Monitoring> getDailyMeasurements(UUID deviceId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay().minusSeconds(1);
        return monitoringRepository.findMeasurementsForDay(deviceId, startOfDay, endOfDay);
    }

    public List<Monitoring> getMonitoring() {
        return monitoringRepository.findAll();
    }

}
