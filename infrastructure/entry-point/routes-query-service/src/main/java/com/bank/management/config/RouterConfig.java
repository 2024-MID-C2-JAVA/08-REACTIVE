package com.bank.management.config;


import com.bank.management.handler.AccountHandler;
import com.bank.management.handler.CustomerHandler;
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
        return route(POST("/api/v1/public/bank-accounts/customer/get-accounts").and(accept(MediaType.APPLICATION_JSON)), handler::getBankAccountByCustomer)
                .andRoute(POST("/api/v1/public/bank-accounts/get").and(accept(MediaType.APPLICATION_JSON)), handler::getBankAccount);
    }

    @Bean
    public RouterFunction<ServerResponse> customerRoutes(CustomerHandler handler) {
        return route(POST("/api/v1/public/customers").and(accept(MediaType.APPLICATION_JSON)), handler::getAllCustomers)
                .andRoute(POST("/api/v1/public/customers/get").and(accept(MediaType.APPLICATION_JSON)), handler::getCustomerById);
    }

}
