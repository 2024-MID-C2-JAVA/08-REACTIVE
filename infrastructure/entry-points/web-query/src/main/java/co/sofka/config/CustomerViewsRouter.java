package co.sofka.config;

import co.sofka.handler.CustomerViewHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class CustomerViewsRouter {
    @Bean(name = "uniqueCustomerRouteView")
    public RouterFunction<ServerResponse> customerRoutesView(CustomerViewHandler customerViewHandler){
        return RouterFunctions.route(POST("/api/customer/get")
                .and(accept(MediaType.APPLICATION_JSON)), customerViewHandler::getCustomerById);
    }
}
