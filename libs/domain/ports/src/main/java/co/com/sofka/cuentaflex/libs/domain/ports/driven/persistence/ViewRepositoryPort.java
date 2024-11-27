package co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence;

import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.data.AccountView;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.data.CustomerView;
import reactor.core.publisher.Mono;

public interface ViewRepositoryPort {
    Mono<Void> saveCustomerView(CustomerView customerView);
    Mono<Void> saveAccountToCustomerView(String customerId, AccountView accountView);
}
