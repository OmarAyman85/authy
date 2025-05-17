package com.example.authy.dto;

import com.example.authy.model.enums.Gender;
import com.example.authy.model.enums.MaritalStatus;
import com.example.authy.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Date;

/**
 * Data Transfer Object (DTO) for user-related operations.
 * Used to carry user data between different layers of the application.
 */
@Getter
@RequiredArgsConstructor
public class UserDTO {

    private final long id;          // Unique identifier for the user
    private final String profilePicture;
    private final String firstName; // User's first name
    private final String middleName; // User's middle name
    private final String lastName;  // User's last name
    private final String email;     // User's email address
    private final String userName;  // User's login username
    private final String password;  // User's password (should be encrypted before storage)
    private final String mobilePhone;
    private final String homePhone;
    private final Date dateOfBirth;
    private final String nationalID;
    private final Gender gender;
    private final MaritalStatus maritalStatus;
    private final Role role;        // User's assigned role (e.g., ADMIN, USER)
    private final String apartment;
    private final String floor;
    private final String street;
    private final String area;
    private final String city;
    private final String country;
    private final String postalCode;
    private final String linkedinUrl;
    private final String githubUrl;
    private final String portfolioUrl;
    private final String facebookUrl;
    private final String instagramUrl;
    private final String xUrl;
    private final String bio;
    private final String interests;
    private final boolean mfaEnabled;
    private final String mfaSecret;
    private final boolean is_email_verified;
    private final String auth_provider;

    // Removed setter methods to ensure immutability and maintain data integrity.
    // Consider using a Builder pattern if object construction needs to be flexible.
}
