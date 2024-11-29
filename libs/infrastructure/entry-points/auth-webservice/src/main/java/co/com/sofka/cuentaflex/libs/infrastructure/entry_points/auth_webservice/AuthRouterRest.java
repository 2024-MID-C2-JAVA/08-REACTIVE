package co.com.sofka.cuentaflex.libs.infrastructure.entry_points.auth_webservice;

import org.springframework.context.annotation.Configuration;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AuthRouterRest {
    /*@Bean
    public RouterFunction<ServerResponse> authRouter(
            RegisterUserHandler registerUserHandler,
            LoginUserHandler loginUserHandler
    ) {
        return route(
                POST(AuthRoutesConstants.REGISTER_ENDPOINT),
                registerUserHandler::handle
        ).andRoute(
                POST(AuthRoutesConstants.LOGIN_ENDPOINT),
                loginUserHandler::handle
        );
    }*/
}
