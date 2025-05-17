package com.example.authy.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * DTO for handling verification code requests.
 * Used in Multi-Factor Authentication (MFA) or email-based verification flows.
 */
@Getter
@RequiredArgsConstructor
public class VerificationRequest {

//    private final String email;    // User's email associated with the verification request
    private final String username; // Username for identity verification
    private final String code;     // Verification code provided by the user

    // Removed setters to ensure immutability and data integrity.
    // If modification is required, consider using a builder pattern.
}
