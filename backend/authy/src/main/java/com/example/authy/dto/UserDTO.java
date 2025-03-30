package com.example.authy.dto;

import com.example.authy.model.enums.Role;
import lombok.Data;

@Data
public class UserDTO {
    private long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private boolean mfaEnabled;
//    private String secret;
    private Role role;
}
