package com.example.restapi.service.impl;

import com.example.restapi.config.security.JWTTokenProvider;
import com.example.restapi.exception.InvalidUsernameOrPasswordException;
import com.example.restapi.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionServiceImpl implements SessionService {
    @Value("${admin.user-id}")
    private int adminUserId;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    private final JWTTokenProvider jwtTokenProvider;

    @Override
    public String signIn(String username, String password) {
        if (username.equals(adminUsername) && password.equals(adminPassword)) {
            return jwtTokenProvider.generateToken(adminUserId + "",
                    List.of("ROLE_ADMIN"));

        } else {
            throw new InvalidUsernameOrPasswordException();
        }
    }
}
