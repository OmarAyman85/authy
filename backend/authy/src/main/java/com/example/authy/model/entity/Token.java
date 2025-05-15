package com.example.authy.model.entity;

import com.example.authy.model.enums.TokenType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing authentication tokens.
 * Tokens are used for authentication and authorization, including access and refresh tokens.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor // Ensures JPA compatibility by providing a no-args constructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for the token record

    @Column(nullable = false, unique = true, length = 512)
    private String token; // The actual token string, stored securely

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenType tokenType; // The type of token (e.g., BEARER, REFRESH)

    @Column(nullable = false)
    private boolean expired; // Indicates whether the token has expired

    @Column(nullable = false)
    private boolean revoked; // Indicates whether the token has been revoked (e.g., user logout)

    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading to optimize performance
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Reference to the associated user

    /**
     * Constructs a new Token with the specified properties.
     *
     * @param token     The token string.
     * @param tokenType The type of the token.
     * @param user      The associated user.
     */
    public Token(String token, TokenType tokenType, User user) {
        this.token = token;
        this.tokenType = tokenType;
        this.user = user;
        this.expired = false;
        this.revoked = false;
    }
}
