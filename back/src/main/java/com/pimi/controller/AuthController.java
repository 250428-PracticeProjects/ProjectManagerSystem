package com.pimi.controller;

import com.pimi.dto.response.LoginDTO;
import com.pimi.dto.request.LoginRequestDTO;
import com.pimi.exceptions.EmailAlreadyTakenException;
import com.pimi.exceptions.InvalidCredentialsException;
import com.pimi.exceptions.InvalidRequestBodyException;
import com.pimi.models.User;
import com.pimi.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> authenticateUser(@RequestBody LoginRequestDTO loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }


    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User registrationDto) {
        return new ResponseEntity<>(authService.registerUser(registrationDto), HttpStatus.CREATED);
    }


    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> invalidCredentialsException(InvalidCredentialsException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(EmailAlreadyTakenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> EmailAlreadyTakenException(EmailAlreadyTakenException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(InvalidRequestBodyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> InvalidRequestBodyException(InvalidRequestBodyException e) {
        return Map.of("error", e.getMessage());
    }



}
