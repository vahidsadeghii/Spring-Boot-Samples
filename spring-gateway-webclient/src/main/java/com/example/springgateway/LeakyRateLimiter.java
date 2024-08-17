package com.example.springgateway;

import org.springframework.cloud.gateway.filter.ratelimit.AbstractRateLimiter;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.support.ConfigurationService;
import org.springframework.context.ApplicationEvent;
import reactor.core.publisher.Mono;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;

public class LeakyRateLimiter extends AbstractRateLimiter {
    private Queue<CompletableFuture<Response>> requestQueue;
    private int bucketSize;
    private int requestPerSeconds;

    protected LeakyRateLimiter(int bucketSize, int requestPerSeconds) {
        super(null, "Leaky-Rate-Limiter", (ConfigurationService) null);
        this.requestQueue = new ArrayBlockingQueue<>(bucketSize);
        this.bucketSize = bucketSize;
        this.requestPerSeconds = requestPerSeconds;

        new Thread(
                () -> {
                    while (true) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        for (int i = 0; i < requestPerSeconds; i++) {
                            try {
                                CompletableFuture<Response> pendingRequest = requestQueue.poll();
                                if (pendingRequest != null) {
                                    System.out.println("Founded a pending request");
                                    pendingRequest.complete(
                                            new Response(true, Collections.emptyMap())
                                    );
                                }
                            } catch (Exception e) {
                            }
                        }
                    }
                }
        ).start();
    }

    @Override
    public Mono<Response> isAllowed(String routeId, String id) {    
        System.out.println("Checking Rate Limiter for Rquest Id: " + id);

        CompletableFuture<Response> request = new CompletableFuture<>();
        boolean offer = requestQueue.offer(request);
        if (!offer) {
            System.out.println("Request rejected, queue filled");

            return Mono.just(
                    new Response(false, Collections.emptyMap())
            );
        } else {
            System.out.println("Request Accepted");

            return Mono.fromFuture(
                    request.whenComplete(
                            (s, throwable) -> {
                                System.out.println("Request Forwarded");
                            }
                    )
            );
        }
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {

    }
}
