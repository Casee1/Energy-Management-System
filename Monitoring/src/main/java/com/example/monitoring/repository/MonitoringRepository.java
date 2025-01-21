package com.example.monitoring.repository;

import com.example.monitoring.entity.Monitoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MonitoringRepository extends JpaRepository<Monitoring, Integer> {

    @Query("SELECT m FROM Monitoring m WHERE m.device_id.deviceId = :deviceId AND m.timestamp BETWEEN :start AND :end")
    List<Monitoring> findMeasurementsForDay(
            @Param("deviceId") UUID deviceId,
            @Param("start") LocalDateTime start,
            @Param("end")LocalDateTime end
        );
}
