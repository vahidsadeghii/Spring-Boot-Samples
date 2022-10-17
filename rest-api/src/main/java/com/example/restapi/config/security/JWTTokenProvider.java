package com.example.restapi.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class JWTTokenProvider implements Serializable {
    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Value("${security.jwt.expiration-in-minutes}")
    private int jwtExpirationInMinutes;

    public String generateToken(String userId, List<String> roles) {
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(jwtExpirationInMinutes);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .setIssuer("TedTalk")
                .setNotBefore(new Date())
                .setId("tedtalk-" + Instant.now().toEpochMilli() + "-" + UUID.randomUUID())
                .claim("roles", String.join(",", roles))
                .compact();
    }

    public TokenInfo resolveToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

            String userId = claims.getSubject();
            List<String> roles = Arrays.asList(claims.get("roles", String.class).split(","));

            return TokenInfo.builder()
                    .userId(Integer.parseInt(userId))
                    .roles(roles)
                    .build();

        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            log.warn("Invalid JWT signature", ex);
            throw new RuntimeException();

        } catch (ExpiredJwtException ex) {
            log.warn("JWT Token Expired", ex);
            throw new RuntimeException();
        }
    }

    boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;

        } catch (MalformedJwtException ex) {
            log.warn("Invalid JWT token", ex);

        } catch (ExpiredJwtException ex) {
            log.warn("Expired JWT token", ex);

        } catch (UnsupportedJwtException ex) {
            log.warn("Unsupported JWT token", ex);

        } catch (IllegalArgumentException ex) {
            log.warn("JWT claims string is empty.", ex);
        }
        return false;
    }

    @Getter
    @Builder
    public static class TokenInfo {
        private int userId;
        private List<String> roles;
    }

}
