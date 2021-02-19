package com.example.authoritybasesecurity.config.security;

public class Credential {
    private String token;
    private int expirationInSeconds;

    public Credential(String token, int expirationInSeconds) {
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
