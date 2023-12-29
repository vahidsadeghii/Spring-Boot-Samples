package com.example.springwebflux;

import jakarta.annotation.security.RolesAllowed;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;

@SpringBootApplication
public class SpringWebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWebfluxApplication.class, args);
    }

    @RestController
    public static class Controller extends BaseController {
        @GetMapping(value = "/open/token")
//        @PreAuthorize("hasAuthority('auth_payment')")
        public Mono<String> handle(ServerWebExchange request) {
//            System.out.println(onlineUser.value);

            return Mono.just("ok").doOnNext(
                    o -> System.out.println("****" + request.getRequest().getHeaders().get("X-Custom"))
            );
        }

        @GetMapping("/api/payment")
        @PreAuthorize("hasAuthority('auth_payment')")
        public Mono<String> handle2(ServerWebExchange request) {
            init(request);

            /*return ReactiveSecurityContextHolder.getContext().map(
                    SecurityContext::getAuthentication
            ).map(
                    authentication -> (OnlineUser) authentication.getPrincipal()
            ).flatMap(
                    onlineUserMono -> {
                        System.out.println("Online User:" + onlineUserMono.id);
                        return Mono.just("ok");
                    }
            );*/

            System.out.println("Online User:" + onlineUser.id);
            return Mono.just("ok");
        }
    }


    @Component
    public static class SecurityWebFilter implements WebFilter {
        @Override
        public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

            if (exchange.getRequest().getHeaders().containsKey("TokenInfo")) {
                OnlineUser onlineUser = new OnlineUser(RandomUtils.nextInt() + "");

                exchange.getAttributes().put("onlineUser", onlineUser);
                return chain.filter(exchange).contextWrite(
                        ReactiveSecurityContextHolder.withAuthentication(
                                new UsernamePasswordAuthenticationToken(onlineUser, null,
                                        Collections.singleton(new SimpleGrantedAuthority("auth_payment")))
                        )
                );
            } else {
                return chain.filter(exchange);
            }
        }
    }

    public static class OnlineUser {
        String id;

        public OnlineUser(String id) {
            this.id = id;
        }
    }
}
