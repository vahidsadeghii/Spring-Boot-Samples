package com.example.springgateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Configuration
public class RouteConfig {
    @Autowired
    private WebClient.Builder webClientBuilder;

    @Bean
    public KeyResolver keyResolver() {
        return exchange -> {
            //Using different key resolver (user id for online user)
            String hostAddress = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            System.out.println(hostAddress);
            return Mono.just(hostAddress);
        };
    }

    @Bean
    public RateLimiter<?> rateLimiter() {
        return new RedisRateLimiter(1, 1, 1);
    }

    @Bean
    public RateLimiter<?> leakyRateLimiter() {
        return new LeakyRateLimiter(2, 1);
    }


    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
        GatewayFilter gatewayFilter =
                (exchange, chain) -> {
                    System.out.println("Request filter executed!");
                    if (exchange.getRequest().getURI().getPath().startsWith("/api")) {
                        Mono<String> stringMono = webClientBuilder.build().get().uri("http://wallet/payment/open/token")
                                .retrieve().bodyToMono(String.class);
                        return
                                stringMono
                                        .doOnNext(value -> {
                                            exchange.getRequest().mutate().header(
                                                    "X-Custom", value
                                            );
                                        })
                                        .map(
                                                s -> {
                                                    return 10;
                                                }
                                        )
                                        .then(
                                                chain.filter(exchange)
                                        );
                    } else {
                        return chain.filter(exchange);
                    }
                };
        return routeLocatorBuilder.routes()
                .route("wallet",
                        r -> r.path("/payment/**")
                                .filters(
                                        filter ->
                                                filter.requestRateLimiter(
                                                                config -> {
                                                                    config.setDenyEmptyKey(false);
                                                                    config.setKeyResolver(keyResolver());
                                                                    config.setRateLimiter(leakyRateLimiter());
                                                                }
                                                        ).filter(gatewayFilter)

                                )
                                .uri("lb://wallet")

                )
                .build();
    }
}
