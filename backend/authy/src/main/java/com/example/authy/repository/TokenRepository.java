package com.example.authy.repository;

import com.example.authy.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Token} entities.
 * Provides methods to retrieve valid tokens and find tokens by their value.
 */
public interface TokenRepository extends JpaRepository<Token, Long> {

    /**
     * Retrieves all valid (non-expired and non-revoked) tokens for a given user.
     *
     * @param userId The ID of the user whose valid tokens are to be retrieved.
     * @return A list of valid {@link Token} entities.
     */
    @Query("""
            SELECT t FROM Token t 
            INNER JOIN t.user u 
            WHERE u.id = :userId AND (t.expired = false OR t.revoked = false)
            """)
    List<Token> findAllValidTokensByUser(@Param("userId") Long userId);

    /**
     * Finds a token by its token string.
     *
     * @param token The token string.
     * @return An {@link Optional} containing the token if found.
     */
    Optional<Token> findByToken(String token);
}
