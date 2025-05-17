package com.example.authy.dto;

import com.example.authy.model.enums.Gender;
import com.example.authy.model.enums.MaritalStatus;
import com.example.authy.model.enums.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;

/**
 * Data Transfer Object (DTO) for transferring user data between application layers.
 * This is an immutable representation of the User entity used in the API layer.
 */
@Getter
@RequiredArgsConstructor // Lombok annotation to generate constructor with all final fields
public class UserDTO {

    private final long id;                      // Unique identifier of the user
    private final String profilePicture;        // URL to the user's profile picture
    private final String firstName;             // First name of the user (mandatory)
    private final String middleName;            // Middle name of the user (optional)
    private final String lastName;              // Last name of the user (mandatory)
    private final String email;                 // Email address (must be unique)
    private final String userName;              // Username used for login (must be unique)
    private final String password;              // Password (should be encrypted before use)
    private final String mobilePhone;           // User's mobile phone number
    private final String homePhone;             // User's home phone number
    private final Date dateOfBirth;             // User's birth date
    private final String nationalID;            // Government-issued national ID number

    private final Gender gender;                // Gender of the user (Enum: MALE, FEMALE, etc.)
    private final MaritalStatus maritalStatus;  // Marital status (Enum: SINGLE, MARRIED, etc.)
    private final Role role;                    // User role (Enum: ADMIN, USER, etc.)

    private final String apartment;             // Apartment number or identifier
    private final String floor;                 // Floor number or level
    private final String street;                // Street address
    private final String area;                  // Local area or neighborhood
    private final String city;                  // City name
    private final String country;               // Country name
    private final String postalCode;            // Postal or ZIP code

    private final String linkedinUrl;           // Link to user's LinkedIn profile
    private final String githubUrl;             // Link to user's GitHub profile
    private final String portfolioUrl;          // Link to user's portfolio site
    private final String facebookUrl;           // Link to user's Facebook profile
    private final String instagramUrl;          // Link to user's Instagram profile
    private final String xUrl;                  // Link to user's X (Twitter) profile

    private final String bio;                   // Short biography or personal summary
    private final String interests;             // User's interests or hobbies

    private final boolean mfaEnabled;           // Whether multi-factor authentication is enabled
    private final String mfaSecret;             // Secret key used for MFA (TOTP)
    private final boolean is_email_verified;    // Whether the user's email is verified
    private final String auth_provider;         // Authentication provider (e.g., LOCAL, GOOGLE)

}
