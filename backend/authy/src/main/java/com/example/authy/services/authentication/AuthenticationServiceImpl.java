package com.example.authy.services.authentication;

import com.example.authy.dto.AuthenticationResponse;
import com.example.authy.dto.UserDTO;
import com.example.authy.dto.VerificationRequest;
import com.example.authy.model.entity.Token;
import com.example.authy.model.entity.User;
import com.example.authy.model.enums.TokenType;
import com.example.authy.repository.TokenRepository;
import com.example.authy.repository.UserRepository;
import com.example.authy.security.jwt.JwtService;
import com.example.authy.security.mfa.TwoFactorAuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TwoFactorAuthenticationService twoFactorAuthenticationService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user with optional Multi-Factor Authentication (MFA).
     *
     * @param request User registration details
     * @return AuthenticationResponse containing tokens and MFA details
     */
    @Override
    public AuthenticationResponse register(UserDTO request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setMfaEnabled(request.isMfaEnabled());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        // Generate MFA secret if MFA is enabled
        if (request.isMfaEnabled()) {
            user.setSecret(twoFactorAuthenticationService.generateNewSecret());
        }

        user = userRepository.save(user);

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(token, user);

        return new AuthenticationResponse(
                token,
                refreshToken,
                user.isMfaEnabled(),
                user.isMfaEnabled() ? twoFactorAuthenticationService.generateQRCode(user.getSecret()) : null
        );
    }

    /**
     * Authenticates a user and issues JWT tokens.
     *
     * @param request User authentication details (username, password)
     * @return AuthenticationResponse with access and refresh tokens, or MFA prompt
     */
    @Override
    public AuthenticationResponse authenticate(UserDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + request.getUsername()));

        // If MFA is enabled, require verification
        if (user.isMfaEnabled()) {
            return new AuthenticationResponse(null, null, true);
        }

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);
        saveUserToken(token, user);

        return new AuthenticationResponse(token, refreshToken, false);
    }

    /**
     * Refreshes authentication token if the provided refresh token is valid.
     *
     * @param request  HTTP request containing refresh token
     * @param response HTTP response
     * @return ResponseEntity with new authentication token or unauthorized status
     * @throws IOException If an error occurs during response handling
     */
    @Override
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String refreshToken = authHeader.substring(7);
        String username = jwtService.getUsernameFromToken(refreshToken);

        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent() && jwtService.isValidRefreshToken(refreshToken, optionalUser.get())) {
            User user = optionalUser.get();
            String newAccessToken = jwtService.generateToken(user);

            revokeAllUserTokens(user);
            saveUserToken(newAccessToken, user);

            return ResponseEntity.ok(new AuthenticationResponse(newAccessToken, refreshToken, user.isMfaEnabled()));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /**
     * Verifies the user's Multi-Factor Authentication (MFA) code.
     *
     * @param verificationRequest Verification details (username, OTP code)
     * @return AuthenticationResponse containing access token if successful
     */
    @Override
    public AuthenticationResponse verifyCode(VerificationRequest verificationRequest) {
        User user = userRepository.findByUsername(verificationRequest.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User not found with username: " + verificationRequest.getUsername()));

        if (!twoFactorAuthenticationService.isOtpValid(user.getSecret(), verificationRequest.getCode())) {
            throw new BadCredentialsException("Invalid verification code.");
        }

        String token = jwtService.generateToken(user);

        revokeAllUserTokens(user);
        saveUserToken(token, user);

        return new AuthenticationResponse(token, null, false);
    }

    /**
     * Revokes all valid tokens associated with a given user.
     *
     * @param user The user whose tokens should be revoked
     */
    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty()) return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }

    /**
     * Stores a newly generated token for a user.
     *
     * @param token JWT token to store
     * @param user  User associated with the token
     */
    private void saveUserToken(String token, User user) {
        Token tokenEntity = new Token();
        tokenEntity.setToken(token);
        tokenEntity.setUser(user);
        tokenEntity.setTokenType(TokenType.BEARER);
        tokenEntity.setExpired(false);
        tokenEntity.setRevoked(false);

        tokenRepository.save(tokenEntity);
    }
}
