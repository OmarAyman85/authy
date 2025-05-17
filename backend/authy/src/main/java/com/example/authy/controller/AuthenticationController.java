package com.example.authy.controller;

import com.example.authy.dto.AuthenticationResponse;
import com.example.authy.dto.UserDTO;
import com.example.authy.dto.VerificationRequest;
import com.example.authy.services.authentication.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Controller responsible for handling authentication-related requests such as user registration, login,
 * verification, and token management.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin("*") // Allows requests from any origin (adjust as needed for security)
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Default endpoint providing a welcome message.
     * @return A welcome string.
     */
    @GetMapping("")
    public String home() {
        return "Welcome to Authy";
    }

    /**
     * Registers a new user.
     * If MFA (Multi-Factor Authentication) is enabled, returns authentication response; otherwise, returns accepted status.
     * @param request User registration details.
     * @return Authentication response if MFA is enabled, otherwise HTTP 202 Accepted.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO request) {
        try {
            var response = authenticationService.register(request);
            return request.isMfaEnabled() ? ResponseEntity.ok(response) : ResponseEntity.accepted().build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
        }
    }


    /**
     * Authenticates a user based on credentials.
     * @param request User credentials.
     * @return JWT authentication response.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody UserDTO request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    /**
     * Endpoint for displaying login message.
     * @return A welcome message for the login page.
     */
    @GetMapping("/login")
    public String login() {
        return "Welcome to Login Page.";
    }

    /**
     * Verifies a user's authentication code for MFA.
     * @param verificationRequest Verification details.
     * @return Response indicating verification success or failure.
     */
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody VerificationRequest verificationRequest) {
        return ResponseEntity.ok(authenticationService.verifyCode(verificationRequest));
    }

    /**
     * Refreshes an authentication token.
     * @param request HTTP request containing refresh token details.
     * @param response HTTP response to be updated with the new token.
     * @return Response containing new authentication token.
     * @throws IOException If an error occurs while processing the request.
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return ResponseEntity.ok(authenticationService.refreshToken(request, response));
    }
}
