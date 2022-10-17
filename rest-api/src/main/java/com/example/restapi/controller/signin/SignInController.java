package com.example.restapi.controller.signin;

import com.example.restapi.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SignInController {
    private final SessionService sessionService;

    @PostMapping("${apis.open.prefix}/signin")
    public SignInResponse handle(@RequestBody SignInRequest request) {
        String token = sessionService.signIn(request.getUsername(), request.getPassword());
        return SignInResponse.builder()
                .token(token)
                .build();
    }
}
