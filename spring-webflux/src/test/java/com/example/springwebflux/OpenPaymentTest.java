package com.example.springwebflux;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(SpringWebfluxApplication.Controller.class)
@Import(SecurityConfiguration.class)
public class OpenPaymentTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    public void openTest(){
        webTestClient.get().uri("/open/token")
                .exchange()
                .expectStatus().isOk();
    }
}
