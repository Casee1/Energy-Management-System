package com.example.devicemanagement.service;

import com.example.devicemanagement.dto.DeviceDTO;
import com.example.devicemanagement.dto.DeviceUserDTO;
import com.example.devicemanagement.entity.Device;
import com.example.devicemanagement.mapper.DeviceMapper;
import com.example.devicemanagement.repository.DeviceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqProducer.class);

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private RabbitMqProducer rabbitMqProducer;

    @Autowired
    private ObjectMapper objectMapper;

    public List<DeviceDTO> getAllDevices() {
        List<Device> devices = (List<Device>) deviceRepository.findAll();
        return devices.stream()
                .map(deviceMapper::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public DeviceDTO insertDevice(DeviceDTO deviceDTO) {
        Device device = deviceMapper.toDevice(deviceDTO);
        Device savedDevice = deviceRepository.save(device);
        return deviceMapper.toDeviceDTO(savedDevice);
    }

    public String deleteDeviceById(UUID id) {
        try {
            deviceRepository.deleteById(id);
            return "Deleted successfully";
        } catch (Exception e) {
            return "Cannot delete device with id " + id;
        }
    }

    public DeviceDTO updateDevice(DeviceDTO deviceDTO) {
        Device existingDevice = deviceRepository.findById(deviceDTO.getId_device())
                .orElseThrow(() -> new RuntimeException("Device not found"));

        if (deviceDTO.getName_device() != null && !deviceDTO.getName_device().equals(existingDevice.getName_device())) {
            existingDevice.setName_device(deviceDTO.getName_device());
        }

        if (deviceDTO.getAddress() != null && !deviceDTO.getAddress().equals(existingDevice.getAddress())) {
            existingDevice.setAddress(deviceDTO.getAddress());
        }

        if (deviceDTO.getDescription() != null && !deviceDTO.getDescription().equals(existingDevice.getDescription())) {
            existingDevice.setDescription(deviceDTO.getDescription());
        }

        if (deviceDTO.getEnergy_consumption() != 0 && deviceDTO.getEnergy_consumption() != existingDevice.getEnergy_consumption()) {
            existingDevice.setEnergy_consumption(deviceDTO.getEnergy_consumption());
        }

        Device updatedDevice = deviceRepository.save(existingDevice);

        return deviceMapper.toDeviceDTO(updatedDevice);
    }

    public void processAndSendToMonitoring(String operation, DeviceDTO deviceDTO) {
        try {
            Map<String, Object> data = new HashMap<>();

            data.put("operation", operation);
            data.put("deviceId", deviceDTO.getId_device());
            data.put("energy", deviceDTO.getEnergy_consumption());



            String message = objectMapper.writeValueAsString(data);

            rabbitMqProducer.sentData(message);
        }catch(Exception e) {
            logger.error("Error while processing and sending data: {}", e.getMessage(), e);
        }

    }

    public void deleteAndSendToMonitoring(String operation, UUID id) {
        try {
            Map<String, Object> data = new HashMap<>();

            data.put("operation", operation);
            data.put("deviceId", id.toString());

            String message = objectMapper.writeValueAsString(data);

            rabbitMqProducer.sentData(message);
        }catch(Exception e) {
            logger.error("Error while processing and sending data: {}", e.getMessage(), e);
        }

    }
}
