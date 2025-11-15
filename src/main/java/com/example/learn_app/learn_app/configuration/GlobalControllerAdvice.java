package com.example.learn_app.learn_app.configuration;

import java.util.logging.ErrorManager;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.learn_app.learn_app.dto.ErrorResponse;
import com.example.learn_app.learn_app.exception.InvalidPasswordException;
import com.example.learn_app.learn_app.exception.UserException;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setField("User");
        errorResponse.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPasswordException(InvalidPasswordException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setField("Password");
        errorResponse.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}