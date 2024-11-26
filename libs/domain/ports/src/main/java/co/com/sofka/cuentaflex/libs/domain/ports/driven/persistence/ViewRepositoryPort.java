package co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence;

import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.data.CustomerView;
import reactor.core.publisher.Mono;

public interface ViewRepositoryPort {
    Mono<Void> saveCustomerView(CustomerView customerView);
}
