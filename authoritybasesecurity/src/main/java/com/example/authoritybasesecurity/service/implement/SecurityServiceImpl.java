package com.example.authoritybasesecurity.service.implement;

import com.example.authoritybasesecurity.config.security.Credential;
import com.example.authoritybasesecurity.config.security.JWTProvider;
import com.example.authoritybasesecurity.exception.InvalidUsernameOrPasswordException;
import com.example.authoritybasesecurity.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityServiceImpl implements SecurityService {
    @Value("${credential.username}")
    private String username;

    @Value("${credential.password}")
    private String password;

    private final JWTProvider jwtProvider;

    @Autowired
    public SecurityServiceImpl(JWTProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Credential signIn(String username, String password) {
        if (!username.equals(this.username) || !password.equals(this.password))
            throw new InvalidUsernameOrPasswordException();

        return jwtProvider.generateToken(username, List.of("GET_BALANCE", "GET_PROFILE"));
    }
}
