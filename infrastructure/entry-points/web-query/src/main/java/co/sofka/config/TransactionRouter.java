package co.sofka.config;

import co.sofka.handler.TransactionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class TransactionRouter {
    @Bean(name = "uniqueTransactionRouterView")
    public RouterFunction<ServerResponse> transactionRouterView(TransactionHandler transactionHandler) {
        return RouterFunctions.route(POST("/api/transaction/get")
                .and(accept(MediaType.APPLICATION_JSON)),transactionHandler::getTransactionsByUserId);
    }
}
