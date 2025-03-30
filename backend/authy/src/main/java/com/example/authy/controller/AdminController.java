package com.example.authy.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @GetMapping
    public String get() {
        return "GET::Admin Controller";
    }

    @PostMapping
    public String post() {
        return "POST::Admin Controller";
    }

    @PutMapping
    public String put() {
        return "PUT::Admin Controller";
    }

    @DeleteMapping
    public String delete() {
        return "DELETE::Admin Controller";
    }

}
