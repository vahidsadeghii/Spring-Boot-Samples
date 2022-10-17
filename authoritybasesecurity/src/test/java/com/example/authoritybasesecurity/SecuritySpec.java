package com.example.authoritybasesecurity;

import com.example.authoritybasesecurity.config.security.Credential;
import com.example.authoritybasesecurity.config.security.JWTProvider;
import com.example.authoritybasesecurity.service.implement.SecurityServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SecuritySpec {
    @Mock
    private JWTProvider jwtProvider;

    @InjectMocks
    private SecurityServiceImpl securityService;

    private String username;
    private String password;

    @BeforeEach
    void setMocks() {
        username = "test";
        password = "password";

        when(jwtProvider.generateToken(username, List.of("GET_BALANCE", "GET_PROFILE"))).thenReturn(
                new Credential("randowm", 10)
        );

        ReflectionTestUtils.setField(securityService, "username", username);
        ReflectionTestUtils.setField(securityService, "password", password);

    }

    @Test
    public void checkSignIn() {
        Assertions.assertNotNull(securityService.signIn(username, password));
        verify(jwtProvider).generateToken(username, List.of("GET_BALANCE", "GET_PROFILE"));
        verify(securityService, timeout(1)).signIn(username, password);
    }

}
