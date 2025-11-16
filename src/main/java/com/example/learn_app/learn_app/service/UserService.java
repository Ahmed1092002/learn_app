package com.example.learn_app.learn_app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.learn_app.learn_app.dto.LoginUser;
import com.example.learn_app.learn_app.dto.UserDto;
import com.example.learn_app.learn_app.dto.UserResponse;
import com.example.learn_app.learn_app.entity.User;
import com.example.learn_app.learn_app.exception.InvalidPasswordException;
import com.example.learn_app.learn_app.exception.UserException;
import com.example.learn_app.learn_app.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(UserDto user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(newUser);
        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setUsername(savedUser.getUsername());
        userResponse.setEmail(savedUser.getEmail());

        return userResponse;
    }

    public boolean verifyPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    public UserResponse loginUser(LoginUser loginUser) {
        Optional<User> userObject = userRepository.findByUsernameOrEmail(loginUser.getUsernameOrEmail(),
                loginUser.getUsernameOrEmail());
        User user = userObject.orElseThrow(() -> new UserException("User Not Find"));
        if (!verifyPassword(user, loginUser.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        return userResponse;

    }
}