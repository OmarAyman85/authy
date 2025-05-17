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
 * Represents a User entity within the authentication system.
 * Integrates with Spring Security by implementing {@link UserDetails}.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor // Required by JPA for instantiation via reflection
@Table(name = "users") // Table name changed to avoid SQL reserved keyword "user"
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // Primary key for the user entity

    @Column(name = "profile_picture")
    private String profilePicture; // Optional URL or base64 string for user’s profile photo

    // Basic personal information
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "middle_name", length = 50)
    private String middleName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    // Authentication-related fields
    @Column(unique = true, nullable = false, length = 100)
    private String email; // Used for login and communication

    @Column(unique = true, nullable = false, length = 50)
    private String username; // Unique system-wide login username

    @Column(nullable = false)
    private String password; // Encrypted password used by Spring Security

    // Contact details
    @Column(name = "Mobile_phone")
    private String mobilePhone;

    @Column(name = "Home_phone")
    private String homePhone;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "national_ID")
    private String nationalID;

    // Enum-based personal attributes
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender; // Enum: MALE, FEMALE, OTHER, etc.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaritalStatus maritalStatus; // Enum: SINGLE, MARRIED, etc.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // Enum representing access level: ADMIN, USER, etc.

    // Address details
    @Column(name = "apartment")
    private String apartment;

    @Column(name = "floor")
    private String floor;

    @Column(name = "street")
    private String street;

    @Column(name = "area")
    private String area;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "postal_code")
    private String postalCode;

    // Social media and portfolio URLs
    @Column(name = "Linkedin")
    private String linkedinUrl;

    @Column(name = "github")
    private String githubUrl;

    @Column(name = "portfolio")
    private String portfolioUrl;

    @Column(name = "facebook")
    private String facebookUrl;

    @Column(name = "instagram")
    private String instagramUrl;

    @Column(name = "X")
    private String xUrl;

    // Additional personal details
    @Column(name = "bio")
    private String bio; // Short biography or about section

    @Column(name = "interests")
    private String interests; // Comma-separated user interests

    // Multi-Factor Authentication (MFA)
    @Column(nullable = false)
    private boolean mfaEnabled; // Flag to determine if MFA is active

    @Column(name = "mfa_secret")
    private String mfaSecret; // Secret key used for generating TOTP codes

    // Email and third-party auth information
    @Column(nullable = false)
    private boolean is_email_verified; // True if the user's email has been verified

    @Column(name = "Auth_provider")
    private String auth_provider; // OAuth provider (e.g., GOOGLE, GITHUB)

    // Token relationship mapping
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Token> tokens; // Associated auth tokens (e.g., JWT refresh tokens)

    // ========== Spring Security UserDetails Implementation ==========

    /**
     * Returns user authorities as a single SimpleGrantedAuthority based on their role.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * Indicates whether the user's account is expired.
     * Always returns true – no expiration logic implemented.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is locked.
     * Always returns true – locking mechanism not implemented.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's password is expired.
     * Always returns true – credential expiration not implemented.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled in the system.
     * Always returns true – no enable/disable toggle implemented.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
