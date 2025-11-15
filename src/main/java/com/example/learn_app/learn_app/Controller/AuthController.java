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
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto createUserRequest, Errors errors) {
        if (errors.hasErrors()) {

            return ResponseEntity.badRequest().body(
                    errors.getFieldErrors().stream().map(err -> {
                        return err.getField() + ": " + err.getDefaultMessage();
                    }).toList());
        }

        UserResponse newUser = userService.createUser(createUserRequest);

        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginUser userDto, Errors errors) {
        if (errors.hasErrors()) {
            if (errors.hasErrors()) {

                return ResponseEntity.badRequest().body(
                        errors.getFieldErrors().stream().map(err -> {
                            return err.getField() + ": " + err.getDefaultMessage();
                        }).toList());
            }
        }
        UserResponse user = userService.loginUser(userDto);
        return ResponseEntity.ok(user);
    }

}
