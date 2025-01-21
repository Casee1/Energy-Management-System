package com.example.usermanagement.controller;

import com.example.usermanagement.dto.UserDTO;
import com.example.usermanagement.entity.Role;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://react.localhost")
//@CrossOrigin(origins = "http://localhost:3000")
//@RequestMapping("/user")
@RequestMapping("/api/v1/auth/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/getUser")
    public List<UserDTO> getAllUser() {
        return userService.getAllUsers();
    }

    @PostMapping("/insertUser")
    public ResponseEntity<User> insertUser(@RequestBody User user) {
        User savedUser = userService.insertUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @DeleteMapping("/deleteUserById/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PutMapping("/updateUser")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        UserDTO updatedUserDTO = userService.updateUser(userDTO);
        return ResponseEntity.ok(updatedUserDTO);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        boolean isValidUser = userService.validateUser(user.getUsername(), user.getPassword());

        if (isValidUser) {
            Role role = userService.getUserRole(user.getUsername());

            if (Role.ADMINISTRATOR.equals(role)) {
                return "Redirect to administrator page";
            } else if (Role.CLIENT.equals(role)) {
                return "Redirect to client page";
            } else {
                return "Login successful but role not recognized";
            }
        } else {
            return "Invalid username or password";
        }
    }

    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello from secured endpoint");
    }
}
