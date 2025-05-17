package com.example.authy.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * DTO representing the response for authentication requests.
 * This includes access and refresh tokens, MFA (Multi-Factor Authentication) status, and optional secret image URI.
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY) // Excludes null or empty fields from JSON response
public class AuthenticationResponse {

    @JsonProperty("access_token") // Maps JSON property to Java field
    private final String accessToken;

    @JsonProperty("refresh_token") // Maps JSON property to Java field
    private final String refreshToken;

    private final boolean mfaEnabled; // Indicates if Multi-Factor Authentication is enabled

    private final String secretImageUri; // Optional: URI for MFA QR code image

    private final String userName;

    /**
     * Constructor for authentication response without a secret image URI.
     *
     * @param accessToken  The JWT access token.
     * @param refreshToken The refresh token.
     * @param mfaEnabled   Whether MFA is enabled.
     */
    public AuthenticationResponse(String accessToken, String refreshToken, boolean mfaEnabled) {
        this(accessToken, refreshToken, mfaEnabled, null, null);
    }

    /**
     * Constructor for authentication response with a secret image URI.
     *
     * @param accessToken    The JWT access token.
     * @param refreshToken   The refresh token.
     * @param mfaEnabled     Whether MFA is enabled.
     * @param secretImageUri The URI for the MFA QR code image.
     */
    public AuthenticationResponse(String accessToken, String refreshToken, boolean mfaEnabled, String secretImageUri, String userName) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.mfaEnabled = mfaEnabled;
        this.secretImageUri = secretImageUri;
        this.userName = userName;
    }
}
