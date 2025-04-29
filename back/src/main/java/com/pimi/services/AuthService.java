package com.pimi.services;

import com.pimi.dto.request.LoginRequestDTO;
import com.pimi.dto.response.LoginDTO;
import com.pimi.models.User;

public interface AuthService {
    LoginDTO login(LoginRequestDTO loginDto);
    User registerUser(User user);
}
