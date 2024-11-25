package com.bank.management.config;

import com.bank.management.handler.AccountHandler;
import com.bank.management.handler.CustomerHandler;
import com.bank.management.handler.EncryptionHandler;
import com.bank.management.handler.TransactionsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> accountRoutes(AccountHandler handler) {
        return route(POST("/api/v1/public/bank-accounts"), handler::createAccount)
                .andRoute(POST("/api/v1/public/bank-accounts/delete").and(accept(MediaType.APPLICATION_JSON)), handler::deleteBankAccount);
    }

    @Bean
    public RouterFunction<ServerResponse> customerRoutes(CustomerHandler handler) {
        return route(POST("/api/v1/public/customers/create").and(accept(MediaType.APPLICATION_JSON)), handler::createCustomer)
                .andRoute(POST("/api/v1/public/customers/delete").and(accept(MediaType.APPLICATION_JSON)), handler::deleteCustomer);
    }

    @Bean
    public RouterFunction<ServerResponse> encryptionRoutes(EncryptionHandler handler) {
        return route(POST("/api/v1/public/encryption/encrypt").and(accept(MediaType.APPLICATION_JSON)), handler::encryptData);
    }

    @Bean
    public RouterFunction<ServerResponse> transactionRoutes(TransactionsHandler handler) {
        return route(POST("/api/v1/private/transactions/deposit").and(accept(MediaType.APPLICATION_JSON)), handler::processDeposit)
                .andRoute(POST("/api/v1/private/transactions/purchase-card").and(accept(MediaType.APPLICATION_JSON)), handler::processPurchase)
                .andRoute(POST("/api/v1/private/transactions/withdraw").and(accept(MediaType.APPLICATION_JSON)), handler::processWithdraw);
    }
}
