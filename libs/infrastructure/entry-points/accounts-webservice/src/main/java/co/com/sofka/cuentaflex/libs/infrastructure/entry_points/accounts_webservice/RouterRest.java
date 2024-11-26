package co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_webservice;

import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_webservice.constants.CustomerEndpointsConstants;
import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_webservice.handlers.CreateCustomerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> commandsRouter(CreateCustomerHandler createCustomerHandler) {
        return route(
                PUT(CustomerEndpointsConstants.CREATE_CUSTOMER_ENDPOINT),
                createCustomerHandler::handle
        );
    }
}
