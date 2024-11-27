package co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_webservice;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.TransactionType;
import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_webservice.constants.AccountEndpointsConstants;
import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_webservice.constants.CustomerEndpointsConstants;
import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_webservice.handlers.CreateAccountHandler;
import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_webservice.handlers.CreateCustomerHandler;
import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_webservice.handlers.PerformDepositBetweenAccountsHandler;
import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_webservice.handlers.PerformUnidirectionalTransactionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> commandsRouter(
            CreateCustomerHandler createCustomerHandler,
            CreateAccountHandler createAccountHandler
    ) {
        return route(
                PUT(CustomerEndpointsConstants.CREATE_CUSTOMER_ENDPOINT),
                createCustomerHandler::handle
        ).andRoute(
                PUT(CustomerEndpointsConstants.CREATE_CUSTOMER_ACCOUNT_ENDPOINT),
                createAccountHandler::handle
        );
    }

    @Bean
    public RouterFunction<ServerResponse> transactionRouter(
            PerformUnidirectionalTransactionHandler performUnidirectionalTransactionHandler,
            PerformDepositBetweenAccountsHandler performDepositBetweenAccountsHandler
    ) {
        return route(
                POST(AccountEndpointsConstants.BRANCH_DEPOSIT_TO_ACCOUNT_URL),
                request -> performUnidirectionalTransactionHandler.handle(request, TransactionType.DEPOSIT_FROM_BRANCH)
        ).andRoute(
                POST(AccountEndpointsConstants.ATM_DEPOSIT_TO_ACCOUNT_URL),
                request -> performUnidirectionalTransactionHandler.handle(request, TransactionType.DEPOSIT_FROM_ATM)
        ).andRoute(
                POST(AccountEndpointsConstants.DEPOSIT_BETWEEN_ACCOUNTS_URL),
                performDepositBetweenAccountsHandler::handle
        ).andRoute(
                POST(AccountEndpointsConstants.ONLINE_PURCHASE_TO_ACCOUNT_URL),
                request -> performUnidirectionalTransactionHandler.handle(request, TransactionType.PURCHASE_ONLINE)
        ).andRoute(
                POST(AccountEndpointsConstants.IN_STORE_PURCHASE_TO_ACCOUNT_URL),
                request -> performUnidirectionalTransactionHandler.handle(request, TransactionType.PURCHASE_IN_STORE)
        ).andRoute(
                POST(AccountEndpointsConstants.ATM_WITHDRAWAL_TO_ACCOUNT_URL),
                request -> performUnidirectionalTransactionHandler.handle(request, TransactionType.WITHDRAW_FROM_ATM)
        );
    }
}
