    package com.example.monitoring.controller;

    import com.example.monitoring.entity.Monitoring;
    import com.example.monitoring.service.MonitoringService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.messaging.handler.annotation.MessageMapping;
    import org.springframework.messaging.handler.annotation.SendTo;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.*;

    import java.time.LocalDate;
    import java.util.List;
    import java.util.UUID;

    @Controller
    @RequestMapping("/api/v1/auth/monitoring")
    @CrossOrigin(origins = "http://react.localhost")
    //@CrossOrigin(origins = "http://localhost:3000")
    public class MonitoringController {

        @Autowired
        private MonitoringService monitoringService;

        @MessageMapping("/check")
        @SendTo("/topic/alerts")
        public String checkEnergyAlert(String message) {
            return message;
        }

        @GetMapping("/{deviceId}/daily")
        public ResponseEntity<List<Monitoring>> getDailyMeasurements(
                @PathVariable UUID deviceId,
                @RequestParam("date") String date
        ) {
            LocalDate parsedDate = LocalDate.parse(date);
            List<Monitoring> measurements = monitoringService.getDailyMeasurements(deviceId, parsedDate);
            return ResponseEntity.ok(measurements);
        }

        @GetMapping("/getMonitoring")
        public ResponseEntity<List<Monitoring>> getMonitoring(){
            List<Monitoring> monitorings = monitoringService.getMonitoring();
            return ResponseEntity.ok(monitorings);
        }

    }
