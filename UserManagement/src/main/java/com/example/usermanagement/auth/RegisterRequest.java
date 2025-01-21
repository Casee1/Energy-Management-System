package com.example.usermanagement.auth;

import com.example.usermanagement.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private Role role;
    private String name;
    private String address;
    private int age;
    private String username;
    private String password;

}
