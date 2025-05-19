package com.example.authy.services.user;

import com.example.authy.dto.UserDTO;

public interface UserService {
    UserDTO getUserDetails(String username);
}
