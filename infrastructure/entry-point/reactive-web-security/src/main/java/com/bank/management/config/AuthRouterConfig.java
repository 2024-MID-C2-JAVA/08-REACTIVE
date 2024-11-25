package com.bank.management.config;

import com.bank.management.handler.AuthHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AuthRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> authRoutes(AuthHandler handler) {
        return route(POST("/auth/v1/login").and(accept(MediaType.APPLICATION_JSON)), handler::login)
                .andRoute(POST("/auth/v1/register").and(accept(MediaType.APPLICATION_JSON)), handler::register);
    }
}
