package com.example.devicemanagement.mapper;

import com.example.devicemanagement.dto.DeviceDTO;
import com.example.devicemanagement.entity.Device;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-15T23:48:05+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20 (Oracle Corporation)"
)
@Component
public class DeviceMapperImpl implements DeviceMapper {

    @Override
    public DeviceDTO toDeviceDTO(Device device) {
        if ( device == null ) {
            return null;
        }

        DeviceDTO deviceDTO = new DeviceDTO();

        deviceDTO.setId_device( device.getId_device() );
        deviceDTO.setName_device( device.getName_device() );
        deviceDTO.setAddress( device.getAddress() );
        deviceDTO.setEnergy_consumption( device.getEnergy_consumption() );
        deviceDTO.setDescription( device.getDescription() );

        return deviceDTO;
    }

    @Override
    public Device toDevice(DeviceDTO deviceDTO) {
        if ( deviceDTO == null ) {
            return null;
        }

        Device device = new Device();

        device.setId_device( deviceDTO.getId_device() );
        device.setName_device( deviceDTO.getName_device() );
        device.setAddress( deviceDTO.getAddress() );
        device.setEnergy_consumption( deviceDTO.getEnergy_consumption() );
        device.setDescription( deviceDTO.getDescription() );

        return device;
    }
}
