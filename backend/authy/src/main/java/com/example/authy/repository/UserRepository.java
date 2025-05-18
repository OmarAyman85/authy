package com.example.authy.repository;

import com.example.authy.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link User} entities.
 * Provides methods to retrieve users by their unique attributes.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their username.
     *
     * @param username The unique username of the user.
     * @return An {@link Optional} containing the user if found.
     */
//    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameOrEmail(String username, String email);

}
