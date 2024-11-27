package co.com.sofka.cuentaflex.libs.domain.use_cases.event_handlers;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.events.CustomerCreatedEvent;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.ViewRepositoryPort;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.data.CustomerView;
import co.com.sofka.cuentaflex.libs.domain.use_cases.ReactiveEventHandler;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public final class CustomerCreatedEventHandler implements ReactiveEventHandler<CustomerCreatedEvent> {
    private final ViewRepositoryPort viewRepositoryPort;

    public CustomerCreatedEventHandler(ViewRepositoryPort viewRepositoryPort) {
        this.viewRepositoryPort = viewRepositoryPort;
    }

    @Override
    public Mono<Void> apply(CustomerCreatedEvent customerCreatedEvent) {
        Objects.requireNonNull(customerCreatedEvent, "The customer created event cannot be null");
        
        return Mono.just(customerCreatedEvent)
                .map(event -> new CustomerView(
                        event.getAggregateRootId(),
                        event.getFirstName(),
                        event.getLastName(),
                        event.getIdentification(),
                        false,
                        LocalDateTime.now(),
                        List.of()
                ))
                .flatMap(viewRepositoryPort::saveCustomerView);
    }
}
