package co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence;

import co.com.sofka.cuentaflex.libs.domain.model.DomainEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventRepositoryPort {
    Mono<Void> save(DomainEvent event);
    Flux<DomainEvent> findByAggregateRootIdAndAggregateName(String aggregateRootId, String aggregateName);
}
