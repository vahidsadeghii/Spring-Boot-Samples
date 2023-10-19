package com.example.springsession;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.SessionTrackingMode;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;

import java.util.EnumSet;

@Configuration
public class ApplicationInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setSessionTrackingModes(
                EnumSet.of(SessionTrackingMode.COOKIE)
        );

        servletContext.getSessionCookieConfig()
                .setSecure(false);

        servletContext.getSessionCookieConfig()
                .setHttpOnly(true);
    }
}
