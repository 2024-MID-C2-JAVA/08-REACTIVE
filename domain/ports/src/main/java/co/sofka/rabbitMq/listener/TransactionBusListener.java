package co.sofka.rabbitMq.listener;

import co.sofka.events.DomainEvent;
import co.sofka.events.TransactionCreatedEvent;
import reactor.core.publisher.Mono;

public interface TransactionBusListener {
    void receive(TransactionCreatedEvent transactionCreatedEvent);
}
