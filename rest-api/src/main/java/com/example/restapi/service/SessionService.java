package com.example.restapi.service;

import com.example.restapi.config.security.JWTTokenProvider;

public interface SessionService {
    String signIn(String username, String password);
}
