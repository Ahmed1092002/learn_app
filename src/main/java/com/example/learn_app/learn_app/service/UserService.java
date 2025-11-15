package com.example.learn_app.learn_app.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.learn_app.learn_app.dto.LoginUser;
import com.example.learn_app.learn_app.dto.UserDto;
import com.example.learn_app.learn_app.dto.UserResponse;
import com.example.learn_app.learn_app.entity.User;
import com.example.learn_app.learn_app.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(UserDto user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        User savedUser = userRepository.save(newUser);
        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setUsername(savedUser.getUsername());
        userResponse.setEmail(savedUser.getEmail());

        return userResponse;
    }

    public boolean verifyPassword(User user, String password) {
        return user.getPassword().equals(password);
    }

    public UserResponse loginUser(LoginUser loginUser) {
        Optional<User> userObject = userRepository.findByUsernameOrEmail(loginUser.getUsernameOrEmail(),
                loginUser.getUsernameOrEmail());
        User user = userObject.orElseThrow(() -> new RuntimeException("User not found"));
        if (!verifyPassword(user, loginUser.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        return userResponse;

    }
}