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

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TwoFactorAuthenticationService twoFactorAuthenticationService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(UserDTO request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setMfaEnabled(request.isMfaEnabled());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(request.getRole());


        if (request.isMfaEnabled()) {
            user.setSecret(twoFactorAuthenticationService.generateNewSecret());
        }

        user = userRepository.save(user);

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(token, user);

        return new AuthenticationResponse(token, refreshToken, user.isMfaEnabled(), twoFactorAuthenticationService.generateQRCode(user.getSecret()));
    }

    public AuthenticationResponse authenticate(UserDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();

        if (user.isMfaEnabled()) {
            return new AuthenticationResponse(null,
                    null,
                    true);
        }

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);
        saveUserToken(token, user);

        return new AuthenticationResponse(token, refreshToken, user.isMfaEnabled());
    }

    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        String username = jwtService.getUsernameFromToken(token);

        if (username != null) {
            var user = userRepository.findByUsername(username)
                    .orElseThrow();
            if (jwtService.isValidRefreshToken(token, user)) {
                String accessToken = jwtService.generateToken(user);
//                String refreshToken = jwtService.generateRefreshToken(user);

                revokeAllUserTokens(user);
                saveUserToken(accessToken, user);
                return new ResponseEntity(new AuthenticationResponse(accessToken, token, user.isMfaEnabled()), HttpStatus.OK);
//                var authResponse = new AuthenticationResponse(accessToken, refreshToken);
//                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//                response.setStatus(HttpServletResponse.SC_OK);
            }
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @Override
    public AuthenticationResponse verifyCode(VerificationRequest verificationRequest) {
        User user = userRepository.findByUsername(verificationRequest.getUsername()).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("User with username %s not found", verificationRequest.getUsername())
                )
        );
        if (twoFactorAuthenticationService.isOtpNotValid(user.getSecret(), verificationRequest.getCode())) {
            throw new BadCredentialsException("Code is not correct!");
        }

        var token = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(token, user);

        return new AuthenticationResponse(token, null, user.isMfaEnabled());
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty()) return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
            tokenRepository.save(token);
        });
    }

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
