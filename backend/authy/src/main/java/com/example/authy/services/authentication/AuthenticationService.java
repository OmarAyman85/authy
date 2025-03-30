package com.example.authy.services.authentication;

import com.example.authy.dto.AuthenticationResponse;
import com.example.authy.dto.UserDTO;
import com.example.authy.dto.VerificationRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

/**
 * Service interface defining authentication-related operations such as user registration,
 * login authentication, token refresh, and multi-factor authentication (MFA) verification.
 */
public interface AuthenticationService {

    /**
     * Registers a new user in the system.
     *
     * @param request The user registration details.
     * @return Authentication response containing tokens and MFA details if applicable.
     */
    AuthenticationResponse register(UserDTO request);

    /**
     * Authenticates a user based on credentials and issues an access token.
     *
     * @param request The user credentials (username, password).
     * @return Authentication response containing access and refresh tokens.
     */
    AuthenticationResponse authenticate(UserDTO request);

    /**
     * Refreshes an authentication token using a valid refresh token.
     *
     * @param request  The HTTP request containing refresh token details.
     * @param response The HTTP response to be updated with the new token.
     * @return Response containing the refreshed authentication token.
     * @throws IOException If an error occurs while processing the request.
     */
    ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * Verifies a user's MFA authentication code.
     *
     * @param verificationRequest The MFA verification details (username/email + OTP code).
     * @return Authentication response indicating whether the verification was successful.
     */
    AuthenticationResponse verifyCode(VerificationRequest verificationRequest);
}
