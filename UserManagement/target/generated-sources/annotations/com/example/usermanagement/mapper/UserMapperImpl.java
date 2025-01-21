package com.example.usermanagement.mapper;

import com.example.usermanagement.dto.UserDTO;
import com.example.usermanagement.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-13T00:16:46+0200",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 20 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.id( userDTO.getId() );
        user.age( userDTO.getAge() );
        user.name( userDTO.getName() );
        user.address( userDTO.getAddress() );
        user.username( userDTO.getUsername() );
        user.password( userDTO.getPassword() );
        user.role( userDTO.getRole() );

        return user.build();
    }

    @Override
    public UserDTO toUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( user.getId() );
        userDTO.setAge( user.getAge() );
        userDTO.setName( user.getName() );
        userDTO.setAddress( user.getAddress() );
        userDTO.setRole( user.getRole() );
        userDTO.setUsername( user.getUsername() );
        userDTO.setPassword( user.getPassword() );

        return userDTO;
    }
}
