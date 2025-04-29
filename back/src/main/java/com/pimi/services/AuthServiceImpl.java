package com.pimi.services;

import com.pimi.dto.request.LoginRequestDTO;
import com.pimi.dto.response.LoginDTO;
import com.pimi.exceptions.EmailAlreadyTakenException;
import com.pimi.exceptions.ForbiddenActionException;
import com.pimi.exceptions.InvalidCredentialsException;
import com.pimi.exceptions.InvalidRequestBodyException;
import com.pimi.models.Role;
import com.pimi.models.Status;
import com.pimi.models.User;
import com.pimi.repos.UserDAO;
import com.pimi.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDAO userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserDAO userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public boolean validateEmail(String email) {

        //Validations:Regex type email
        if (email == null || email.isEmpty()) {
            return false;
        }

        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }

    public boolean validatePassword(String password) {
            /*
                Validations
                // Length >= 8
                // At least 1 lower case character
                // At least 1 Upper case character
                // At least 1 digit
                // At least 1 Special char of the pull (!@#$%^&*)
             */

        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasSpecial = false;
        boolean hasDigit = false;

        String specialChars = "!@#$%^&*";

        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (specialChars.indexOf(c) != -1) {
                hasSpecial = true;
            }
        }

        return hasLower && hasUpper && hasSpecial && hasDigit;
    }

    // Availability

    public boolean isEmailAvailable(String email) {
        return userRepository.findUserByEmail(email).isEmpty();
    }

    @Override
    public LoginDTO login(LoginRequestDTO loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );


        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        User user = userRepository.findUserByEmail(loginDto.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        if(user.getStatus().equals(Status.INACTIVE)){
            throw  new ForbiddenActionException("This user has been inactivated by the Admin. Please contact technical support");
        }

        return new LoginDTO(user, token);
    }

    @Override
    public User registerUser(User registrationUser) {

        if (!validateEmail(registrationUser.getEmail())) {
            throw new InvalidRequestBodyException("Invalid email format");
        }

        if (!isEmailAvailable(registrationUser.getEmail())) {
            throw new EmailAlreadyTakenException("Email already exists!");
        }

        if (!validatePassword(registrationUser.getPassword())) {
            throw new InvalidRequestBodyException("The password must have at least 8 characters, 1 Lower case, 1 UpperCase, 1 digit and 1 Special char (!@#$%^&*)");
        }

        registrationUser.setPassword(passwordEncoder.encode(registrationUser.getPassword()));
        registrationUser.setStatus(Status.ACTIVE);
        registrationUser.setRole(Role.USER);
        return userRepository.save(registrationUser);

    }
}
