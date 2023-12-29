package com.example.springgateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Collections;

@Configuration
public class RouteConfig {
    @Autowired
    private WebClient.Builder webClientBuilder;
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
        GatewayFilter gatewayFilter =
                (exchange, chain) -> {
                    if(exchange.getRequest().getURI().getPath().startsWith("/api")) {
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
        byte[] a = new byte[10];
        return routeLocatorBuilder.routes()
                .route("wallet",
                        r -> r.path("/payment/**")
                                .filters(
                                        filter ->
                                                filter.filter(gatewayFilter)
                                )
                                .uri("lb://wallet")

                )
                .build();
    }
}
