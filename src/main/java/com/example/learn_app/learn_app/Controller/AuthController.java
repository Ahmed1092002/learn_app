package com.example.learn_app.learn_app.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learn_app.learn_app.dto.ErrorResponse;
import com.example.learn_app.learn_app.dto.LoginUser;
import com.example.learn_app.learn_app.dto.UserDto;
import com.example.learn_app.learn_app.dto.UserResponse;
import com.example.learn_app.learn_app.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserDto createUserRequest) {
       

        UserResponse newUser = userService.createUser(createUserRequest);

        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@Valid @RequestBody LoginUser userDto) {

        UserResponse user = userService.loginUser(userDto);
        return ResponseEntity.ok(user);
    }

}
