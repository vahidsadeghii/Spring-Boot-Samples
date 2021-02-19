package com.example.authoritybasesecurity.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JWTProvider {

    @Value("${security.jwt.signature}")
    private String jwtSignature;

    @Value("${security.jwt.token.validity-in-minutes}")
    private int tokenValidity;

    public Credential generateToken(String subject, List<String> authorities) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("subject", subject);
        claims.put("authorities", authorities);

        String token = Jwts.builder()
                .setSubject(subject)
                .addClaims(claims)
                .setExpiration(Date.from(Instant.ofEpochMilli(System.currentTimeMillis() + tokenValidity * 60 * 1000)))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS512, jwtSignature)
                .compact();

        return new Credential(token, tokenValidity * 60);
    }

    public UserDetails validateTokenAndGetUserDetail(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSignature).parseClaimsJws(token).getBody();

        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return ((List<String>) claims.get("authorities")).stream().map(
                        SimpleGrantedAuthority::new
                ).collect(Collectors.toList());
            }

            @Override
            public String getPassword() {
                return null;
            }

            @Override
            public String getUsername() {
                return (String) claims.get("subject");
            }

            @Override
            public boolean isAccountNonExpired() {
                return false;
            }

            @Override
            public boolean isAccountNonLocked() {
                return false;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return false;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };

    }
}
