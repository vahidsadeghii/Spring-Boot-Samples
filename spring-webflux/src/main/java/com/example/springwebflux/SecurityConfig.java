package com.example.springwebflux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity()
public class SecurityConfig {
    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v3 (OpenAPI)
            "/v2/API-docs",
            "/v3/API-docs",
            "/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui/**",

    };

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.cors(
                        ServerHttpSecurity.CorsSpec::disable
                ).formLogin(
                        ServerHttpSecurity.FormLoginSpec::disable
                ).httpBasic(
                        ServerHttpSecurity.HttpBasicSpec::disable
                ).authorizeExchange(
                        authorizeExchangeSpec -> {
                            authorizeExchangeSpec.pathMatchers(
                                            AUTH_WHITELIST
                                    ).permitAll()
                                    .pathMatchers("/api/**").authenticated();
                        }
                ).addFilterBefore(new SecurityWebFilter(), SecurityWebFiltersOrder.AUTHORIZATION)
                .build();
    }
}
