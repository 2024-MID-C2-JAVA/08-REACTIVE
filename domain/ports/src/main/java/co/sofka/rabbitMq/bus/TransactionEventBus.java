package co.sofka.rabbitMq.bus;

import co.sofka.events.TransactionCreatedEvent;
import reactor.core.publisher.Mono;

public interface TransactionEventBus {
    Mono<TransactionCreatedEvent> publishEvent(TransactionCreatedEvent event);
}
