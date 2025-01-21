package com.example.devicemanagement.controller;

import com.example.devicemanagement.dto.DeviceDTO;
import com.example.devicemanagement.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth/device")
@CrossOrigin(origins = "http://react.localhost")
//@CrossOrigin(origins = "http://localhost:3000")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping("/getDevices")
    public ResponseEntity<List<DeviceDTO>> getAllDevices() {
        List<DeviceDTO> deviceDTOs = deviceService.getAllDevices();
        return ResponseEntity.ok(deviceDTOs);
    }

    @ResponseBody
    @PostMapping("/insertDevice")
    public ResponseEntity<DeviceDTO> insertDevice(@RequestBody DeviceDTO deviceDTO) {
        DeviceDTO savedDeviceDTO = deviceService.insertDevice(deviceDTO);

        return ResponseEntity.ok(savedDeviceDTO);
    }

    @ResponseBody
    @PutMapping("/updateDevice")
    public ResponseEntity<DeviceDTO> updateDevice(@RequestBody DeviceDTO deviceDTO) {
        DeviceDTO updatedDeviceDTO = deviceService.updateDevice(deviceDTO);
        deviceService.processAndSendToMonitoring("UPDATE", updatedDeviceDTO);
        return ResponseEntity.ok(updatedDeviceDTO);
    }

    @ResponseBody
    @DeleteMapping("/deleteDeviceById/{id}")
    public ResponseEntity<String> deleteDeviceById(@PathVariable UUID id) {
        deviceService.deleteDeviceById(id);
        deviceService.deleteAndSendToMonitoring("DELETE", id);
        return ResponseEntity.ok("Device deleted successfully");
    }
}
