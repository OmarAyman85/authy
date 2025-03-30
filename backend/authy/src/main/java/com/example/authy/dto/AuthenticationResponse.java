package com.example.authy.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuthenticationResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    private boolean mfaEnabled;

    private String secretImageUri;

    public AuthenticationResponse(String accessToken, String refreshToken, boolean mfaEnabled) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.mfaEnabled = mfaEnabled;
    }

    public AuthenticationResponse(String accessToken, String refreshToken, boolean mfaEnabled, String secretImageUri) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.mfaEnabled = mfaEnabled;
        this.secretImageUri = secretImageUri;
    }
}
