package com.example.authoritybasesecurity.controller.login;

import com.example.authoritybasesecurity.config.security.Credential;
import com.example.authoritybasesecurity.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final SecurityService securityService;

    @Autowired
    public LoginController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @PostMapping("/login")
    public LoginCredentialResponse handle(@RequestBody LoginRequest request) {
        Credential credential = securityService.signIn(request.getUsername(), request.getPassword());

        return new LoginCredentialResponse(credential.getToken(), credential.getExpirationInSeconds());
    }
}
