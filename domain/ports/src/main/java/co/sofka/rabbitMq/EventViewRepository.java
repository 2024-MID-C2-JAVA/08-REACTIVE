package co.sofka.rabbitMq;

import co.sofka.events.DomainEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventViewRepository {
    Flux<DomainEvent> getAll(DomainEvent domainEvent);
}
