package com.example.authy.model.entity;

import com.example.authy.model.enums.Gender;
import com.example.authy.model.enums.MaritalStatus;
import com.example.authy.model.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Entity representing a user in the authentication system.
 * Implements {@link UserDetails} to integrate with Spring Security.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor // Ensures JPA compatibility by providing a no-args constructor
@Table(name = "users") // Renamed to avoid conflicts with SQL reserved words
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // Unique identifier for the user

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName; // User's first name

    @Column(name = "middle_name", length = 50)
    private String middleName; // User's middle name

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName; // User's last name

    @Column(unique = true, nullable = false, length = 100)
    private String email; // Unique email for contact and verification

    @Column(unique = true, nullable = false, length = 50)
    private String username; // Unique username for login

    @Column(nullable = false)
    private String password; // Encrypted password for authentication

    @Column(name = "Mobile_phone")
    private String mobilePhone;

    @Column(name = "Home_phone")
    private String homePhone;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "national_ID")
    private String nationalID;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender; // User gender (e.g., MALE, FEMALE)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaritalStatus maritalStatus; // User marital status (e.g., SINGLE, MARRIED)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // User role (e.g., ADMIN, USER)

    @Column(name="apartment")
    private String apartment;

    @Column(name="floor")
    private String floor;

    @Column(name="street")
    private String street;

    @Column(name="area")
    private String area;

    @Column(name="city")
    private String city;

    @Column(name="country")
    private String country;

    @Column(name="postal_code")
    private String postalCode;

    @Column(name="Linkedin")
    private String linkedinUrl;

    @Column(name="github")
    private String githubUrl;

    @Column(name="portfolio")
    private String portfolioUrl;

    @Column(name="facebook")
    private String facebookUrl;

    @Column(name="instagram")
    private String instagramUrl;

    @Column(name="X")
    private String xUrl;

    @Column(name="bio")
    private String bio;

    @Column(name="interests")
    private String interests;

    @Column(nullable = false)
    private boolean mfaEnabled; // Indicates whether Multi-Factor Authentication is enabled

    @Column(name="mfa_secret")
    private String mfaSecret; // Secret key for MFA (if applicable)

    @Column(nullable = false)
    private boolean is_email_verified; // Indicates whether Multi-Factor Authentication is enabled

    @Column(name="Auth_provider")
    private String auth_provider; // Indicates whether Multi-Factor Authentication is enabled

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Token> tokens; // List of associated authentication tokens

    /**
     * Returns a collection of granted authorities based on the user's role.
     *
     * @return A list containing the user's role as an authority.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * Indicates whether the user's account is expired.
     * Currently, all accounts are considered non-expired.
     *
     * @return {@code true} since accounts do not expire in this implementation.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is locked.
     * Currently, all accounts are considered non-locked.
     *
     * @return {@code true} since accounts are not locked in this implementation.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) are expired.
     * Currently, all credentials are considered non-expired.
     *
     * @return {@code true} since credentials do not expire in this implementation.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled.
     * Currently, all users are considered enabled.
     *
     * @return {@code true} since all users are enabled in this implementation.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
