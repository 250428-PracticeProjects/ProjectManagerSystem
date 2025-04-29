package com.pimi.controller;

import com.pimi.exceptions.*;
import com.pimi.models.User;
import com.pimi.security.CustomUserDetails;
import com.pimi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping()
    public User updateUserHandler(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody User user) {
        return userService.updateUser(userDetails.getUserId(), user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("inactive/{userId}")
    public User inActivatedUser(@AuthenticationPrincipal CustomUserDetails userDetails,@PathVariable int userId) {
        return userService.inActivatedUser(userDetails.getUserId(), userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("active/{userId}")
    public User activatedUser(@AuthenticationPrincipal CustomUserDetails userDetails,@PathVariable int userId) {
        return userService.activatedUser(userDetails.getUserId(), userId);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserHandler(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.deleteUser(userDetails.getUserId());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> resourceNotFoundHandler(ResourceNotFoundException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(EmailAlreadyTakenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> EmailAlreadyTakenException(EmailAlreadyTakenException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(InvalidRequestBodyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> invalidRequestBodyException(InvalidRequestBodyException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(UnauthenticatedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> unauthenticatedException(UnauthenticatedException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(ForbiddenActionException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> ForbiddenActionException(ForbiddenActionException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> AccessDeniedException(AccessDeniedException e) {
        return Map.of("error", e.getMessage());
    }
}
