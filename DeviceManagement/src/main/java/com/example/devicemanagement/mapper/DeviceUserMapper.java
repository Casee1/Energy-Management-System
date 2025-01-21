package com.example.devicemanagement.mapper;

import com.example.devicemanagement.dto.DeviceUserDTO;
import com.example.devicemanagement.entity.Device;
import com.example.devicemanagement.entity.DeviceUser;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface DeviceUserMapper {

    DeviceUser toDeviceUser(DeviceUserDTO deviceUserDTO);
    DeviceUserDTO toDeviceUserDTO(DeviceUser deviceUser);

}
