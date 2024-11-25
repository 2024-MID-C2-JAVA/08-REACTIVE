package co.sofka.config;

import co.sofka.handler.CustomerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class CustomerRouter {
    @Bean
    public RouterFunction<ServerResponse> customerRoutes(CustomerHandler customerHandler){
        return RouterFunctions.route(POST("/api/customer/delete")
                .and(accept(MediaType.APPLICATION_JSON)),customerHandler::deleteCustomer)
                .andRoute(POST("/api/customer/create")
                        .and(accept(MediaType.APPLICATION_JSON)),customerHandler::getCustomerById);
    }
}
