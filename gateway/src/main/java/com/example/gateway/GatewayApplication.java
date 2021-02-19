package com.example.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Weigher;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.function.Consumer3;
import rx.Producer;

import java.io.PrintStream;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        GatewayFilter modifyHeaderFilter = (exchange, chain) -> {
            String ip = exchange.getRequest().getHeaders().getFirst("X-Forward-For");

            if (StringUtils.isEmpty(ip))
                ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();

            String agentType = exchange.getRequest().getHeaders().getFirst("User-Agent");
            String requestId = UUID.randomUUID().toString();

            ServerHttpRequest request;
            try {
                request = exchange.getRequest().mutate().header(
                        "Client-Info",
                        new ObjectMapper().writeValueAsString(
                                ClientInfo.builder()
                                        .ip(ip)
                                        .agentType(agentType)
                                        .requestId(requestId)
                                        .build()
                        )
                ).build();
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error in create client info");
            }

            return chain.filter(exchange.mutate().request(request).build());
        };

        RewriteFunction<EncryptedRequest, String> modifyBodyFilter = (exchange, encryptedRequest) -> {
            if (exchange.getRequest().getMethod() == HttpMethod.PUT ||
                    exchange.getRequest().getMethod() == HttpMethod.POST) {
                String decryptValue = AES.decrypt(
                        encryptedRequest.getEncryptedValue(),
                        "123456789abcdefg"
                );

                return Mono.just(decryptValue);

            } else return Mono.empty();
        };

        return builder.routes()
                .route(
                        predicateSpec ->
                                predicateSpec.path("/servicea/**")
                                        .filters(
                                                filter ->
                                                        filter.filter(modifyHeaderFilter)
                                                                .modifyRequestBody(EncryptedRequest.class, String.class, modifyBodyFilter)
                                        ).uri("lb://servicea")
                                        .id("servicea")
                )
                .route(
                        predicateSpec ->
                                predicateSpec.path("/serviceb/**")
                                        .filters(
                                                filter ->
                                                        filter.filter(modifyHeaderFilter)
                                                                .modifyRequestBody(EncryptedRequest.class, String.class, modifyBodyFilter)
                                        )
                                        .uri("lb://serviceb")
                                        .id("serviceb")

                )
                .build();
    }

    public void handle() {

    }
}
