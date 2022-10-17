package com.example.restapi.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityContextFilter extends OncePerRequestFilter {
    private final JWTTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            authorizationHeader = authorizationHeader.substring(7);
            addAuthenticationObjectIntoContext(authorizationHeader);
        }
        filterChain.doFilter(request, response);
    }

    private void addAuthenticationObjectIntoContext(String authorizationHeader) {
        JWTTokenProvider.TokenInfo tokenInfo = jwtTokenProvider.resolveToken(authorizationHeader);
        OnlineUser onlineUser = new OnlineUser(tokenInfo.getUserId(), tokenInfo.getRoles());

        SecurityContextHolder.getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                onlineUser, null, onlineUser.getAuthorities()));
    }

}
