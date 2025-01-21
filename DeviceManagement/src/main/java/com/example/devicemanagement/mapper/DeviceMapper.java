package com.example.devicemanagement.mapper;


import com.example.devicemanagement.dto.DeviceDTO;
import com.example.devicemanagement.entity.Device;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    DeviceDTO toDeviceDTO(Device device);

    Device toDevice(DeviceDTO deviceDTO);

}
