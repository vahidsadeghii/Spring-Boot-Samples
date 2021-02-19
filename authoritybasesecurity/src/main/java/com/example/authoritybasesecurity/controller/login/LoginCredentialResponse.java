package com.example.authoritybasesecurity.controller.login;

public class LoginCredentialResponse {
    private String token;
    private int expirationInSeconds;

    public LoginCredentialResponse(String token, int expirationInSeconds) {
        this.token = token;
        this.expirationInSeconds = expirationInSeconds;
    }

    public String getToken() {
        return token;
    }

    public int getExpirationInSeconds() {
        return expirationInSeconds;
    }
}
