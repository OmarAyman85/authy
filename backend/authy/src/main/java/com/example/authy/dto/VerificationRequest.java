package com.example.authy.dto;

import lombok.Data;

@Data
public class VerificationRequest {
    private String email;
    private String username;
    private String code;
}
