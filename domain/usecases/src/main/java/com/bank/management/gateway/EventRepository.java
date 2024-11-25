package com.bank.management.gateway;

import com.bank.management.generic.DomainEvent;
import reactor.core.publisher.Mono;

public interface EventRepository {
    public Mono<DomainEvent> save(DomainEvent event);
}
