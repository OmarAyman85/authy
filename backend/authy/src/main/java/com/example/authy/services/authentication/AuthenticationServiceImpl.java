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
import jakarta.servlet.http.Cookie;
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
import java.util.Arrays;
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
        user.setProfilePicture(request.getProfilePicture());
        user.setFirstName(request.getFirstName());
        user.setMiddleName(request.getMiddleName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setUsername(request.getUserName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setMobilePhone(request.getMobilePhone());
        user.setHomePhone(request.getHomePhone());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setNationalID(request.getNationalID());
        user.setGender(request.getGender());
        user.setMaritalStatus(request.getMaritalStatus());
        user.setRole(request.getRole());
        user.setApartment(request.getApartment());
        user.setFloor(request.getFloor());
        user.setStreet(request.getStreet());
        user.setArea(request.getArea());
        user.setCity(request.getCity());
        user.setCountry(request.getCountry());
        user.setPostalCode(request.getPostalCode());
        user.setLinkedinUrl(request.getLinkedinUrl());
        user.setGithubUrl(request.getGithubUrl());
        user.setPortfolioUrl(request.getPortfolioUrl());
        user.setFacebookUrl(request.getFacebookUrl());
        user.setInstagramUrl(request.getInstagramUrl());
        user.setXUrl(request.getXUrl());
        user.setBio(request.getBio());
        user.setInterests(request.getInterests());

        user.setMfaEnabled(request.isMfaEnabled());
        // Generate MFA secret if MFA is enabled
        if (request.isMfaEnabled()) {
            user.setMfaSecret(twoFactorAuthenticationService.generateNewSecret());
        }

        user.set_email_verified(request.is_email_verified());
        user.setAuth_provider(request.getAuth_provider());

        user = userRepository.save(user);

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(token, user);

        return new AuthenticationResponse(
                token,
                user.isMfaEnabled(),
                user.isMfaEnabled() ? twoFactorAuthenticationService.generateQRCode(user.getMfaSecret()) : null,
                user.isMfaEnabled() ? user.getUsername() : null
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
        // 1. Authenticate user credentials using Spring Security's AuthenticationManager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
        );

        System.out.println("Attempting to authenticate: " + request.getUserName());

        Optional<User> userOpt = userRepository.findByUsernameOrEmail(request.getUserName(), request.getUserName());
        System.out.println("User found: " + userOpt.isPresent());

        // 2. Retrieve the user from the database
        User user = userRepository.findByUsernameOrEmail(request.getUserName(),request.getUserName())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User not found with username or email: " + request.getUserName()
                ));

        // 3. If Multi-Factor Authentication (MFA) is enabled, prompt for additional verification
        if (user.isMfaEnabled()) {
            // Returning null tokens and MFA required flag = true
            return new AuthenticationResponse(null, true);
        }

        // 4. Generate access and refresh tokens
        String token = jwtService.generateToken(user);
//        String refreshToken = jwtService.generateRefreshToken(user);
        // 5. Revoke any previously active tokens for this user to maintain security
        revokeAllUserTokens(user);

        // 6. Persist the new access token to the database
        saveUserToken(token, user);

        // 7. Return the access tokens and indicate that MFA is not required
        return new AuthenticationResponse(token, false);
    }

    /**
     * Refreshes authentication token if the provided refresh token is valid.
     *
     * @param request  HTTP request containing refresh token
//     * @param response HTTP response
     * @return ResponseEntity with new authentication token or unauthorized status
//     * @throws IOException If an error occurs during response handling
     */

    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            System.out.println("********************************************");
            System.out.println("********************************************");
            System.out.println("No cookies found in request");
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            System.out.println("********************************************");
            System.out.println("********************************************");
            System.out.println("Cookie found: " + cookie.getName() + " = " + cookie.getValue());
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        System.out.println("********************************************");
        System.out.println("********************************************");
        System.out.println("refreshToken cookie not found");
        return null;
    }


    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = getRefreshTokenFromCookie(request);

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = jwtService.getUsernameFromToken(refreshToken);
        Optional<User> optionalUser = userRepository.findByUsernameOrEmail(username, username);

        if (optionalUser.isPresent() && jwtService.isValidRefreshToken(refreshToken, optionalUser.get())) {
            User user = optionalUser.get();
            String newAccessToken = jwtService.generateToken(user);
            String newRefreshToken = jwtService.generateRefreshToken(user);

            revokeAllUserTokens(user);
            saveUserToken(newAccessToken, user);

            // Set new refresh token cookie (HttpOnly, Secure, etc.)
            Cookie cookie = new Cookie("refreshToken", newRefreshToken);
            cookie.setHttpOnly(true);
            cookie.setSecure(false); // if using HTTPS
            cookie.setPath("/"); // path scope
            response.addCookie(cookie);

            return ResponseEntity.ok(new AuthenticationResponse(newAccessToken, user.isMfaEnabled()));
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
        User user = userRepository.findByUsernameOrEmail(verificationRequest.getUsername(), verificationRequest.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User not found with username: " + verificationRequest.getUsername()));

        if (!twoFactorAuthenticationService.isOtpValid(user.getMfaSecret(), verificationRequest.getCode())) {
            throw new BadCredentialsException("Invalid verification code.");
        }

        String token = jwtService.generateToken(user);

        revokeAllUserTokens(user);
        saveUserToken(token, user);

        return new AuthenticationResponse(token, true, null, null);
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
