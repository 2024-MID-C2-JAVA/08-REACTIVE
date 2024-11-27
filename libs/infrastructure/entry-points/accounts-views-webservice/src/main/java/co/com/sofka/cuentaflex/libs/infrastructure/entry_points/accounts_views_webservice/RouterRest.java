package co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_views_webservice;

import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_views_webservice.constants.CustomerViewEndpointsConstants;
import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_views_webservice.handlers.GetCustomerAccountHandler;
import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_views_webservice.handlers.GetCustomerAccountsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> queriesRouter(
            GetCustomerAccountHandler getCustomerAccountHandler,
            GetCustomerAccountsHandler getCustomerAccountsHandler
    ) {
        return route(
                POST(CustomerViewEndpointsConstants.GET_CUSTOMER_ENDPOINT),
                getCustomerAccountsHandler::handle
        ).andRoute(
                POST(CustomerViewEndpointsConstants.GET_CUSTOMER_ACCOUNT_ENDPOINT),
                getCustomerAccountHandler::handle
        );
    }
}
