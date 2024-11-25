package co.sofka.router;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class RouterRest {

    private static final Logger logger = LoggerFactory.getLogger(RouterRest.class);

    public RouterRest() {
        logger.info("RouterRest - constructor");
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        logger.info("RouterRest - routerFunction");
        return route(
                POST("/api/v1/CreateCustomer"),
                handler::SaveCustomerUseCase
        )
                .andRoute(
                POST("/api/v1/transaction/depositSucursal"),
                handler::SaveDepositarSucursalUseCase
        )
                .andRoute(
                        POST("/api/v1/transaction/depositCajero"),
                        handler::SaveDepositarCajeroUseCase
                )
                .andRoute(
                        POST("/api/v1/transaction/depositTransferencia"),
                        handler::SaveDepositarTransferenciaUseCase
                )
                .andRoute(
                        POST("/api/v1/transaction/retiroATM"),
                        handler::SaveRetiroCajeroUseCase
                )
                .andRoute(
                        POST("/api/v1/transaction/compra"),
                        handler::SaveCompraUseCase
                )
                ;
    }

}
