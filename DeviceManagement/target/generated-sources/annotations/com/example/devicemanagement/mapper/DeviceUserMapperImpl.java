package com.example.devicemanagement.mapper;

import com.example.devicemanagement.dto.DeviceUserDTO;
import com.example.devicemanagement.entity.DeviceUser;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-15T23:48:05+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 20 (Oracle Corporation)"
)
@Component
public class DeviceUserMapperImpl implements DeviceUserMapper {

    @Override
    public DeviceUser toDeviceUser(DeviceUserDTO deviceUserDTO) {
        if ( deviceUserDTO == null ) {
            return null;
        }

        DeviceUser deviceUser = new DeviceUser();

        deviceUser.setId_commontable( deviceUserDTO.getId_commontable() );
        deviceUser.setDevice( deviceUserDTO.getDevice() );
        deviceUser.setId_user( deviceUserDTO.getId_user() );

        return deviceUser;
    }

    @Override
    public DeviceUserDTO toDeviceUserDTO(DeviceUser deviceUser) {
        if ( deviceUser == null ) {
            return null;
        }

        DeviceUserDTO deviceUserDTO = new DeviceUserDTO();

        deviceUserDTO.setId_commontable( deviceUser.getId_commontable() );
        deviceUserDTO.setDevice( deviceUser.getDevice() );
        deviceUserDTO.setId_user( deviceUser.getId_user() );

        return deviceUserDTO;
    }
}
