package com.example.springwebflux;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;

@Configurable
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity.authorizeExchange(
                        authorizeExchangeSpec -> {
                            authorizeExchangeSpec
                                    .pathMatchers("/api/**").authenticated()
                                    .anyExchange().permitAll();
                        }
                ).httpBasic(
                        httpBasicSpec -> {
                            httpBasicSpec.disable();
                            httpBasicSpec.authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED));
                        }
                )
                .formLogin(
                        formLoginSpec -> {
                            formLoginSpec.disable();
                            formLoginSpec.authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED));
                        }
                )
                .addFilterBefore(new SpringWebfluxApplication.SecurityWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .csrf(
                        ServerHttpSecurity.CsrfSpec::disable
                )
                .build();
    }
}
