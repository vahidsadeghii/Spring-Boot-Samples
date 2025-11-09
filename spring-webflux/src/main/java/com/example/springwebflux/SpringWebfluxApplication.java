package com.example.springwebflux;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;


@SpringBootApplication
public class SpringWebfluxApplication{
    public static void main(String[] args) {
        SpringApplication.run(SpringWebfluxApplication.class, args);
    }

    @RestController
    public static class Controller extends BaseController {
        @GetMapping(value = "/open/token")
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


    public static class OnlineUser {
        String id;

        public OnlineUser(String id) {
            this.id = id;
        }
    }
}
