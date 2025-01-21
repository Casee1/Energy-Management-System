package com.example.usermanagement.mapper;


import com.example.usermanagement.dto.UserDTO;
import com.example.usermanagement.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserDTO userDTO);
    UserDTO toUserDTO(User user);
}
