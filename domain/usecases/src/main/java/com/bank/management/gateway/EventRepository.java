package com.bank.management.gateway;

import com.bank.management.generic.DomainEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventRepository {
    public Mono<DomainEvent> save(DomainEvent event);
    public Flux<DomainEvent> findById(String aggregateRootId);
}
