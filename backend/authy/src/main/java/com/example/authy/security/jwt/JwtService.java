package com.example.authy.security.jwt;

import com.example.authy.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * Service responsible for handling JWT operations such as token generation, validation, and claim extraction.
 */
@Service
public class JwtService {

    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    @Value("${JWT_EXPIRATION}")
    private long jwtExpiration;

    @Value("${REFRESH_EXPIRATION}")
    private long refreshExpiration;

    /**
     * Extracts the username (subject) from the given JWT token.
     *
     * @param token The JWT token.
     * @return The username embedded in the token.
     */
    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Validates the given access token by checking if it matches the user and is not expired.
     *
     * @param token The JWT access token.
     * @param user  The user details to validate against.
     * @return {@code true} if the token is valid, otherwise {@code false}.
     */
    public boolean isValidToken(String token, UserDetails user) {
        String username = getUsernameFromToken(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Validates the given refresh token by checking if it matches the user and is not expired.
     *
     * @param token The JWT refresh token.
     * @param user  The user entity to validate against.
     * @return {@code true} if the token is valid, otherwise {@code false}.
     */
    public boolean isValidRefreshToken(String token, User user) {
        return isValidToken(token, user);
    }

    /**
     * Checks if the given JWT token is expired.
     *
     * @param token The JWT token.
     * @return {@code true} if the token is expired, otherwise {@code false}.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the given JWT token.
     *
     * @param token The JWT token.
     * @return The expiration date of the token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the given JWT token using the provided resolver function.
     *
     * @param token   The JWT token.
     * @param resolver Function to extract the required claim.
     * @param <T>     The type of the extracted claim.
     * @return The extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    /**
     * Extracts all claims from the given JWT token.
     *
     * @param token The JWT token.
     * @return The claims contained within the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Generates a new access token for the given user.
     *
     * @param user The user details for whom the token is generated.
     * @return A JWT access token.
     */
    public String generateToken(UserDetails user) {
        return buildToken(user, jwtExpiration);
    }

    /**
     * Generates a new refresh token for the given user.
     *
     * @param user The user details for whom the token is generated.
     * @return A JWT refresh token.
     */
    public String generateRefreshToken(UserDetails user) {
        return buildToken(user, refreshExpiration);
    }

    /**
     * Builds a JWT token with the specified expiration time.
     *
     * @param user       The user details for whom the token is generated.
     * @param expiration The expiration duration in milliseconds.
     * @return A signed JWT token.
     */
    private String buildToken(UserDetails user, long expiration) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Generates the signing key used to sign JWT tokens.
     *
     * @return The secret key for signing tokens.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
