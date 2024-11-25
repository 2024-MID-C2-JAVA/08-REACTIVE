package co.sofka.rabbitMq;

import co.sofka.events.DomainEvent;
import reactor.core.publisher.Mono;

public interface EventRepository {
    Mono<DomainEvent> save(DomainEvent domainEvent);
}
