package com.example.devicemanagement.controller;

import com.example.devicemanagement.dto.DeviceUserDTO;
import com.example.devicemanagement.entity.Device;
import com.example.devicemanagement.entity.DeviceUser;
import com.example.devicemanagement.service.DeviceService;
import com.example.devicemanagement.service.DeviceUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth/deviceuser")
@CrossOrigin(origins = "http://react.localhost")
//@CrossOrigin(origins = "http://localhost:3000")
public class DeviceUserController {

    @Autowired
    private DeviceUserService deviceUserService;

    @GetMapping("/getDeviceUser/{userId}")
    public List<DeviceUserDTO> getAllDevicesUser(@PathVariable UUID userId) {
        return deviceUserService.getDevicesByUserId(userId);
    }

    @PostMapping("/createDeviceUser")
    public ResponseEntity<DeviceUserDTO> createDeviceUser(@RequestBody DeviceUserDTO deviceUserDTO) {
        ResponseEntity<DeviceUserDTO> deviceUserDTOResponseEntity = deviceUserService.createDeviceUser(deviceUserDTO);
        deviceUserService.processAndSendToMonitoring("CREATE", deviceUserDTO);
        return deviceUserDTOResponseEntity;
    }

    @DeleteMapping("/deleteDeviceUserById/{idUser}")
    public String deleteDeviceUserById(@PathVariable UUID idUser) {
        return deviceUserService.deleteDeviceUserByUserId(idUser);
    }
}
