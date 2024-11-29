package co.sofka.config;


import co.sofka.handler.AccountViewsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class AccountViewsRouter {
    @Bean(name = "uniqueAccountRouterViewFunction")
    public RouterFunction<ServerResponse> accountRouterViewFunction(AccountViewsHandler handler) {
        return RouterFunctions.route(POST("/api/account/get")
                .and(accept(MediaType.APPLICATION_JSON)),handler::getAccountById);
    }
}
