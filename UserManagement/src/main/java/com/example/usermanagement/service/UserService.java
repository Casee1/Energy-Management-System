package com.example.usermanagement.service;

import com.example.usermanagement.dto.UserDTO;
import com.example.usermanagement.entity.Role;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.mapper.UserMapper;
import com.example.usermanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RabbitMqEvents rabbitMqEvents;



    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toUserDTO).collect(Collectors.toList());
    }

    public User insertUser(User user) {
        return userRepository.save(user);
    }


    public String deleteUserById(UUID id) {
        try {
            logger.info("Attempting to delete user with ID: {}", id);
            userRepository.deleteById(id);
            logger.info("User deleted successfully, sending delete event for ID: {}", id);
            rabbitMqEvents.sendUserDeletedEvent(id);
            return "Deleted successfully";
        } catch (Exception e) {
            logger.error("Error deleting user with ID: {}", id, e);
            return "Cannot delete user with id " + id;
        }
    }

    public UserDTO updateUser(UserDTO userDTO) {
        User existingUser = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userDTO.getName() != null && !userDTO.getName().equals(existingUser.getName())) {
            existingUser.setName(userDTO.getName());
        }

        if (userDTO.getAddress() != null && !userDTO.getAddress().equals(existingUser.getAddress())) {
            existingUser.setAddress(userDTO.getAddress());
        }

        if (userDTO.getAge() != 0 && userDTO.getAge() != existingUser.getAge()) {
            existingUser.setAge(userDTO.getAge());
        }

        if (userDTO.getUsername() != null && !userDTO.getUsername().equals(existingUser.getUsername())) {
            existingUser.setUsername(userDTO.getUsername());
        }

        if (userDTO.getPassword() != null && !userDTO.getPassword().equals(existingUser.getPassword())) {
            existingUser.setPassword(userDTO.getPassword());
        }


        User updatedUser = userRepository.save(existingUser);
        return userMapper.toUserDTO(updatedUser);
    }

    public boolean validateUser(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getPassword().equals(password);
        }

        return false;
    }

    public Role getUserRole(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            return user.get().getRole();
        }

        return null;
    }
}
