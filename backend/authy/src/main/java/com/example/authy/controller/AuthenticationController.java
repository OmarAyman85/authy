package com.example.authy.controller;

import com.example.authy.dto.AuthenticationResponse;
import com.example.authy.dto.UserDTO;
import com.example.authy.dto.VerificationRequest;
import com.example.authy.services.authentication.AuthenticationService;
import com.example.authy.services.profilePicture.ProfilePictureService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Controller responsible for handling authentication-related requests such as user registration, login,
 * verification, and token management.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200") // Allows requests from any origin (adjust as needed for security)
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final ProfilePictureService profilePictureService;

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
        System.out.println("Login API hit: " + request.getUserName());
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

    @PostMapping("/upload-profile-picture")
    public ResponseEntity<String> uploadProfilePicture(
            @RequestParam("file") MultipartFile file
    ) {
        try {
            String fileUrl = profilePictureService.storeProfilePicture(file);
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
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
