package com.example.restapi.controller.signin;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
class SignInResponse {
    private final String token;
}
