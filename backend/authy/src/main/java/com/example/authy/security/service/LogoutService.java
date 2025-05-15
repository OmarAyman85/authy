package com.example.authy.security.service;

import com.example.authy.repository.TokenRepository;
import com.example.authy.model.entity.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

/**
 * Service responsible for handling user logout by revoking authentication tokens.
 */
@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    /**
     * Logs out a user by revoking and expiring their JWT token.
     *
     * @param request        The HTTP request containing the authorization header.
     * @param response       The HTTP response (not used but included for interface compliance).
     * @param authentication The authentication object (not used in this implementation).
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = extractToken(request);

        if (token == null) {
            return; // No valid token found, nothing to process.
        }

        tokenRepository.findByToken(token).ifPresent(this::revokeToken);
    }

    /**
     * Extracts the JWT token from the Authorization header.
     *
     * @param request The HTTP request containing headers.
     * @return The JWT token if present and valid, otherwise null.
     */
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Extract token after "Bearer "
        }

        return null;
    }

    /**
     * Marks a given token as expired and revoked.
     *
     * @param token The token entity to be updated.
     */
    private void revokeToken(Token token) {
        token.setRevoked(true);
        token.setExpired(true);
        tokenRepository.save(token);
    }
}
