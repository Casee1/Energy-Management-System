package com.example.sensorsimulation.service;

import com.example.sensorsimulation.entity.SensorData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVReader {

    private final RabbitMqProducer producer;
    private final ObjectMapper objectMapper;

    public CSVReader(RabbitMqProducer producer, ObjectMapper objectMapper) {
        this.producer = producer;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void readAndSimulate() {
        List<Double> measurements = new ArrayList<>();
        String deviceId = readDeviceIdFromFile("C:\\Users\\casia\\OneDrive\\Desktop\\DS proiect\\deviceId.json");

        if (deviceId == null) {
            System.err.println("Device ID not found in the JSON file.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader("sensor.csv"))) {
            String line;


            while ((line = br.readLine()) != null) {
                double measurementValue = Double.parseDouble(line.trim());
                measurements.add(measurementValue);

                SensorData sensorData = new SensorData();
                sensorData.setTimestamp(Instant.now().toEpochMilli());
                sensorData.setDeviceId(deviceId);
                sensorData.setMeasurement_value(measurementValue);

                String message = objectMapper.writeValueAsString(sensorData);

                producer.sendSensorData(message);


                Thread.sleep(3000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readDeviceIdFromFile(String filePath) {
        try {
            String json = new String(Files.readAllBytes(Paths.get(filePath))); // Read file content
            JsonNode jsonNode = objectMapper.readTree(json); // Parse JSON content
            return jsonNode.get("deviceId").asText(); // Extract the deviceId value
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
