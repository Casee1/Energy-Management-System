    package com.example.monitoring.service;

    import com.example.monitoring.entity.DeviceMonitoring;
    import com.example.monitoring.entity.Monitoring;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.amqp.rabbit.annotation.RabbitListener;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.messaging.simp.SimpMessagingTemplate;
    import org.springframework.stereotype.Component;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Map;
    import java.util.UUID;
    import java.util.concurrent.atomic.AtomicInteger;


    @Component
    public class RabbitMqConsumer {

        private static final Logger logger = LoggerFactory.getLogger(RabbitMqConsumer.class);

        private int count = 0;
        List<Double> values = new ArrayList<Double>();

        @Autowired
        private MonitoringService monitoringService;

        @Autowired
        private DeviceMonitoringService deviceMonitoringService;

        private final ObjectMapper objectMapper;
        private final AtomicInteger idGenerator = new AtomicInteger(1);

        @Autowired
        private SimpMessagingTemplate messagingTemplate;

        public RabbitMqConsumer(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @RabbitListener(queues = "sensor_data")
        public void consumeSensorData(String message) {
            logger.info(" [x] Received: {}", message);
            try {

                Map<String, Object> data = objectMapper.readValue(message, Map.class);

                long timestamp = ((Number) data.get("timestamp")).longValue();
                double measurementValue = ((Number) data.get("measurement_value")).doubleValue();
                String idDevice = (String) data.get("deviceId");
                UUID deviceId = UUID.fromString(idDevice);

                DeviceMonitoring deviceMonitoring = deviceMonitoringService.findByDeviceId(deviceId);

                Monitoring monitoring = new Monitoring();
                count++;
                values.add(measurementValue);
                if(count == 6) {
                    double sum = 0;
                    for(int i=0; i<values.size(); i++) {
                        sum += values.get(i);
                    }
                    monitoring.setId(idGenerator.getAndIncrement());
                    monitoring.setTimestamp(String.valueOf(timestamp));
                    monitoring.setMeasurement((float) sum);
                    monitoring.setDevice_id(deviceMonitoring);
                    monitoringService.addMonitoring(monitoring);


                    if (deviceMonitoring != null && sum > deviceMonitoring.getMax_energy_consumption()) {
                        String notificationMessage = String.format("Device %s exceeded max energy limit. Value: %.2f",
                                deviceId, sum);

                        messagingTemplate.convertAndSend("/topic/alerts", notificationMessage);
                        logger.warn(notificationMessage);
                    }

                    count = 0;
                    values.clear();
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }

        @RabbitListener(queues = "monitoring_queue")
        public void consumerDeviceData(String message) {
            logger.info("Received message: {}", message);
            try {
                Map<String, Object> data = objectMapper.readValue(message, Map.class);

                String operation = (String) data.get("operation");
                String idDevice = (String) data.get("deviceId");
                UUID deviceId = UUID.fromString(idDevice);

                if ("UPDATE".equalsIgnoreCase(operation)) {
                    int energyConsumption = Integer.parseInt(data.get("energy").toString());

                    DeviceMonitoring deviceMonitoring = new DeviceMonitoring();
                    deviceMonitoring.setDeviceId(deviceId);
                    deviceMonitoring.setMax_energy_consumption(energyConsumption);

                    deviceMonitoringService.addDeviceMonitoring(deviceMonitoring);
                    logger.info("Device monitoring data updated: {}", deviceMonitoring);

                } else if ("DELETE".equalsIgnoreCase(operation)) {
                    deviceMonitoringService.deleteDeviceMonitoring(deviceId);
                    logger.info("Device monitoring data deleted for deviceId: {}", deviceId);

                } else if ("CREATE".equalsIgnoreCase(operation)) {
                    int energyConsumption = Integer.parseInt(data.get("energy").toString());
                    String idUser = (String) data.get("userId");
                    UUID userId = UUID.fromString(idUser);

                    DeviceMonitoring deviceMonitoring = new DeviceMonitoring();
                    deviceMonitoring.setDeviceId(deviceId);
                    deviceMonitoring.setMax_energy_consumption(energyConsumption);
                    deviceMonitoring.setUser_id(userId);

                    deviceMonitoringService.addDeviceMonitoring(deviceMonitoring);
                    logger.info("Device monitoring data created: {}", deviceMonitoring);
                } else {
                    logger.warn("Unknown operation: {}", operation);
                }
            } catch (Exception e) {
                logger.error("Error processing message: {}", e.getMessage(), e);
            }
        }

    }
