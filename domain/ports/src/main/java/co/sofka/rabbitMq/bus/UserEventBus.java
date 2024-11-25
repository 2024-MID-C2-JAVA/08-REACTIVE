package co.sofka.rabbitMq.bus;

import co.sofka.events.CreateUserEvent;
import co.sofka.events.DomainEvent;
import reactor.core.publisher.Mono;

public interface UserEventBus {
    Mono<DomainEvent>publishEvent(CreateUserEvent createUserEvent);
}
