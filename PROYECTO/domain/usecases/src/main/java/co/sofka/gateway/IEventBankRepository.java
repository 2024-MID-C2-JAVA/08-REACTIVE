package co.sofka.gateway;

import co.sofka.Event;
import co.sofka.generic.DomainEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface IEventBankRepository {
    Flux<DomainEvent> findById(String aggregateId);
    Mono<DomainEvent> save(DomainEvent event);
}
