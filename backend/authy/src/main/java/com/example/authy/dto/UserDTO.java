package com.example.authy.dto;

import com.example.authy.model.enums.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Data Transfer Object (DTO) for user-related operations.
 * Used to carry user data between different layers of the application.
 */
@Getter
@RequiredArgsConstructor
public class UserDTO {

    private final long id;          // Unique identifier for the user
    private final String username;  // User's login username
    private final String email;     // User's email address
    private final String firstName; // User's first name
    private final String lastName;  // User's last name
    private final String password;  // User's password (should be encrypted before storage)
    private final boolean mfaEnabled; // Indicates if Multi-Factor Authentication (MFA) is enabled
    private final Role role;        // User's assigned role (e.g., ADMIN, USER)

    // Removed setter methods to ensure immutability and maintain data integrity.
    // Consider using a Builder pattern if object construction needs to be flexible.
}
