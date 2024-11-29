package co.sofka.config;

import co.sofka.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class UserRouter {
    @Bean
    public RouterFunction<ServerResponse>userRoute(UserHandler userHandler) {
        return RouterFunctions.route(POST("/api/auth/register")
                .and(accept(MediaType.APPLICATION_JSON)),userHandler::register)
                .andRoute(POST("/api/auth/authenticate")
                        .and(accept(MediaType.APPLICATION_JSON)),userHandler::authenticate);
    }
}
