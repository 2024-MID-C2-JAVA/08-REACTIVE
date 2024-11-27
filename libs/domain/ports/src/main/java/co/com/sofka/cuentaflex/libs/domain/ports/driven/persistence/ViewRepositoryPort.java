package co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence;

import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.data.AccountTransactionView;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.data.AccountView;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.data.CustomerView;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ViewRepositoryPort {
    Mono<Void> saveCustomerView(CustomerView customerView);
    Mono<Void> saveAccountToCustomerView(String customerId, AccountView accountView);
    Mono<Void> saveAccountTransactionToAccountView(String customerId, String accountId, AccountTransactionView accountTransactionView);
    Mono<Void> updateAccountBalance(String customerId, String accountId, BigDecimal balance);
    Mono<AccountView> getAccountView(String customerId, String accountId);

    Mono<CustomerView> getCustomerView(String customerId);
}
