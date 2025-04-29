package com.pimi.dto.response;

import com.pimi.models.Role;
import com.pimi.models.User;

public class LoginDTO {

    private int userId;

    private String firstName;

    private String lastName;

    private String email;


    private Role role;


    private String token;


    public LoginDTO(){}


    public LoginDTO(User user, String token){
        this.userId=user.getUserId();
        this.firstName= user.getFirstName();
        this.lastName= user.getLastName();
        this.email= user.getEmail();
        this.role=user.getRole();
        this.token=token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
