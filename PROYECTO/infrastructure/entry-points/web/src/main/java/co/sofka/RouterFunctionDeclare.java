//package co.sofka;
//
//import co.sofka.command.create.SaveCustumerHandler;
//import co.sofka.command.query.CustomerByUserNameHandler;
//import co.sofka.command.query.ListAllCustomerHandler;
//import lombok.AllArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.server.RequestPredicates;
//import org.springframework.web.reactive.function.server.RouterFunction;
//import org.springframework.web.reactive.function.server.RouterFunctions;
//import org.springframework.web.reactive.function.server.ServerResponse;
//
//@Component
//@AllArgsConstructor
//public class RouterFunctionDeclare {
//
//    private static final Logger logger = LoggerFactory.getLogger(RouterFunctionDeclare.class);
//
//    private final ListAllCustomerHandler handler;
//
//    private final SaveCustumerHandler saveCustumerHandler;
//
//    private final CustomerByUserNameHandler customerByUserNameHandler;
//
//    @Bean
//    public RouterFunction<ServerResponse> routerFunction() {
//        return RouterFunctions
//                .route(RequestPredicates.POST("/client/all"), handler.getAll())
//                .andRoute(RequestPredicates.POST("/client/save"), saveCustumerHandler::save)
//                .andRoute(RequestPredicates.POST("/client/{username}"), customerByUserNameHandler::getCustomerByUsername);
//    }
//
//
//
//}
