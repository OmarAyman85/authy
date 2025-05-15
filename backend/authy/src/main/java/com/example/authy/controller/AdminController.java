package com.example.authy.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for administrative operations.
 * This controller is secured and accessible only by users with the 'ADMIN' authority.
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN')") // Ensures only ADMIN users can access this controller
public class AdminController {

    /**
     * Handles GET requests for admin-related operations.
     * @return a response message indicating a successful GET request
     */
    @GetMapping
    public String get() {
        return "GET::Admin Controller";
    }

    /**
     * Handles POST requests for creating admin-related resources.
     * @return a response message indicating a successful POST request
     */
    @PostMapping
    public String post() {
        return "POST::Admin Controller";
    }

    /**
     * Handles PUT requests for updating admin-related resources.
     * @return a response message indicating a successful PUT request
     */
    @PutMapping
    public String put() {
        return "PUT::Admin Controller";
    }

    /**
     * Handles DELETE requests for removing admin-related resources.
     * @return a response message indicating a successful DELETE request
     */
    @DeleteMapping
    public String delete() {
        return "DELETE::Admin Controller";
    }
}
