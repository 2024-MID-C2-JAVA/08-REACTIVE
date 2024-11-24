package co.sofka.config;

import co.sofka.handler.AccountHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class AccountRouter {
    @Bean
    public RouterFunction<ServerResponse> accountRouterFunction(AccountHandler accountHandler) {
        return RouterFunctions.route(POST("/api/account/create")
                .and(accept(MediaType.APPLICATION_JSON)),accountHandler::createAccount)
                .andRoute(POST("/api/account/delete")
                        .and(accept(MediaType.APPLICATION_JSON)), accountHandler::deleteAccount)
                .andRoute(POST("/api/account/get")
                        .and(accept(MediaType.APPLICATION_JSON)),accountHandler::getAccountById);
    }
}
