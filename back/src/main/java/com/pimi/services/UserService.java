package com.pimi.services;

import com.pimi.exceptions.EmailAlreadyTakenException;
import com.pimi.exceptions.ForbiddenActionException;
import com.pimi.exceptions.InvalidRequestBodyException;
import com.pimi.exceptions.ResourceNotFoundException;
import com.pimi.models.Status;
import com.pimi.models.User;
import com.pimi.repos.UserDAO;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO){
        this.userDAO=userDAO;
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
        return userDAO.findUserByEmail(email).isEmpty();
    }

    public boolean isEmailAvailableForUpdate(int userId, String email) {
        Optional<User> u = userDAO.findUserByEmail(email);

        if (u.isEmpty()) {
            return true;
        }

        User user = u.get();

        return user.getUserId() == userId;
    }

    public User updateUser(int userId, User updatedUser){
        Optional<User> existingUserOptional = userDAO.findById(userId);

        if (existingUserOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        if (!validateEmail(updatedUser.getEmail())) {
            throw new InvalidRequestBodyException("Invalid email format");
        }

        if (!isEmailAvailableForUpdate(userId,updatedUser.getEmail())) {
            throw new EmailAlreadyTakenException("Email already exists!");
        }

        if (!validatePassword(updatedUser.getPassword()) && !updatedUser.getPassword().isEmpty()) {
            throw new InvalidRequestBodyException("The password must have at least 8 characters, 1 Lower case, 1 UpperCase, 1 digit and 1 Special char (!@#$%^&*)");
        }

        User existingUser = existingUserOptional.get();
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());

        if(!updatedUser.getPassword().isEmpty()){
            existingUser.setPassword(updatedUser.getPassword());
        }

        return userDAO.save(existingUser);
    }

    public User inActivatedUser(int userAdminId, int userId){

        if(userAdminId == userId){
            throw  new ForbiddenActionException("You are not allow to inActive this user");
        }

        Optional<User> existingUserOptional = userDAO.findById(userId);

        if (existingUserOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        User existingUser = existingUserOptional.get();
        existingUser.setStatus(Status.INACTIVE);

        return userDAO.save(existingUser);
    }

    public User activatedUser(int userAdminId, int userId){

        if(userAdminId == userId){
            throw  new ForbiddenActionException("You are not allow to inActive this user");
        }

        Optional<User> existingUserOptional = userDAO.findById(userId);

        if (existingUserOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        User existingUser = existingUserOptional.get();
        existingUser.setStatus(Status.ACTIVE);

        return userDAO.save(existingUser);
    }


    public void deleteUser(int userId){
        Optional<User> existingUserOptional = userDAO.findById(userId);

        if(existingUserOptional.isEmpty()){
            throw  new ResourceNotFoundException("User not found with id: "+userId);
        }

        userDAO.deleteById(userId);
    }
}
