package com.example.springsession;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringSessionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSessionApplication.class, args);
    }

    @RestController
    public static class  APIs {
        private final SecurityContextRepository securityContextRepository;

        public APIs(SecurityContextRepository securityContextRepository) {
            this.securityContextRepository = securityContextRepository;
        }


        public record OnlineUser(String username, String password) { }
        @GetMapping("/login")
        public String login(HttpServletRequest request, HttpServletResponse response){
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    UsernamePasswordAuthenticationToken.authenticated(
                            new OnlineUser("username", "password"), null,
                            List.of(new SimpleGrantedAuthority("transactions"))
                    );



            SecurityContextHolderStrategy contextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

            SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
            emptyContext.setAuthentication(usernamePasswordAuthenticationToken);

            contextHolderStrategy.setContext(emptyContext);
            securityContextRepository.saveContext(emptyContext, request, response);

            return "ok";
        }

        @GetMapping("/api/transactions")
        @PreAuthorize("hasAuthority('transactions')")
        public List<String> transactions(){
            return Arrays.asList("ok", "nok");
        }

        @GetMapping("/api/profile")
        @PreAuthorize("hasAuthority('profile')")
        public String profile(){
            return "My Profile";
        }
    }
}
