package co.sofka.rabbitMq.bus;

import co.sofka.events.AccountCreatedEvent;
import co.sofka.events.DomainEvent;
import reactor.core.publisher.Mono;

public interface AccountEventBus {
    Mono<DomainEvent> publishEvent(AccountCreatedEvent accountCreatedEvent);
}
