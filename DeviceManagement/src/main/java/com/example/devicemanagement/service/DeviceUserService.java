package com.example.devicemanagement.service;

import com.example.devicemanagement.dto.DeviceDTO;
import com.example.devicemanagement.dto.DeviceUserDTO;
import com.example.devicemanagement.entity.Device;
import com.example.devicemanagement.entity.DeviceUser;
import com.example.devicemanagement.mapper.DeviceUserMapper;
import com.example.devicemanagement.repository.DeviceRepository;
import com.example.devicemanagement.repository.DeviceUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeviceUserService {
    private static final Logger logger = LoggerFactory.getLogger(DeviceUserService.class);


    @Autowired
    private DeviceUserRepository deviceUserRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceUserMapper deviceUserMapper;

    @Autowired
    private RabbitMqProducer rabbitMqProducer;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${CONFIG_PATH}")
    private String deviceIdFile;



    /*public List<DeviceUserDTO> getAllDevicesUser() {
        return deviceUserRepository.findAll().stream()
                .map(deviceUserMapper::toDeviceUserDTO)
                .collect(Collectors.toList());
    }*/

    public List<DeviceUserDTO> getDevicesByUserId(UUID userId) {
        return deviceUserRepository.findByIdUser(userId).stream()
                .map(deviceUserMapper::toDeviceUserDTO)
                .collect(Collectors.toList());
    }

    public ResponseEntity<DeviceUserDTO> createDeviceUser(DeviceUserDTO deviceUserDTO) {

        Device device = deviceRepository.findById(deviceUserDTO.getDevice().getId_device())
                .orElseThrow(() -> new RuntimeException("Device not found"));

        DeviceUser deviceUser = deviceUserMapper.toDeviceUser(deviceUserDTO);
        deviceUser.setDevice(device);
        deviceUser.setId_user(deviceUserDTO.getId_user());
        deviceUserRepository.save(deviceUser);

        DeviceUserDTO responseDto = deviceUserMapper.toDeviceUserDTO(deviceUser);

        return ResponseEntity.of(Optional.of(responseDto));
    }

    public String deleteDeviceUserByUserId(UUID idUser) {
        try {
            logger.info("Deleting DeviceUser with user ID: {}", idUser);
            deviceUserRepository.deleteByIdUser(idUser);
            logger.info("Successfully deleted DeviceUser with user ID: {}", idUser);
            return "Deleted successfully";
        } catch (Exception e) {
            logger.error("Cannot delete DeviceUser with user ID: {}", idUser, e);
            return "Cannot delete deviceUser with id " + idUser;
        }
    }

    public void processAndSendToMonitoring(String operation, DeviceUserDTO deviceUserDTO) {
        try {
            Device device = deviceRepository.findById(deviceUserDTO.getDevice().getId_device())
                    .orElseThrow(() -> new RuntimeException("Device not found"));
            deviceUserDTO.getDevice().setEnergy_consumption(device.getEnergy_consumption());

            Map<String, Object> data = new HashMap<>();

            data.put("operation", operation);
            data.put("deviceId", deviceUserDTO.getDevice().getId_device());
            data.put("energy", deviceUserDTO.getDevice().getEnergy_consumption());
            data.put("userId", deviceUserDTO.getId_user());

            String message = objectMapper.writeValueAsString(data);

            rabbitMqProducer.sentData(message);

            saveDeviceIdToFile(deviceUserDTO.getDevice().getId_device());

        }catch(Exception e) {
            logger.error("Error while processing and sending data: {}", e.getMessage(), e);
        }

    }

    private void saveDeviceIdToFile(UUID deviceId) {
        try {
            Map<String, Object> deviceData = new HashMap<>();
            deviceData.put("deviceId", deviceId);

            String jsonContent = objectMapper.writeValueAsString(deviceData);

            File file = new File(deviceIdFile);

            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(jsonContent);
            }

            logger.info("Device ID saved to file: {}", file.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Error saving device ID to file: {}", e.getMessage(), e);
        }
    }
}
