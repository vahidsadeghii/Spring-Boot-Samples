package com.example.authoritybasesecurity.service;

import com.example.authoritybasesecurity.config.security.Credential;

public interface SecurityService {
    Credential signIn(String username, String password);
}
