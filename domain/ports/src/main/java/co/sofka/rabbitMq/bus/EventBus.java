package co.sofka.rabbitMq.bus;

import co.sofka.events.AccountCreatedEvent;
import co.sofka.events.CreateUserEvent;
import co.sofka.events.DomainEvent;
import co.sofka.events.TransactionCreatedEvent;
import reactor.core.publisher.Mono;

public interface EventBus {
    Mono<TransactionCreatedEvent> publishTransactionEvent(TransactionCreatedEvent event);
    Mono<DomainEvent>publishUserEvent(CreateUserEvent createUserEvent);
    Mono<DomainEvent> publishAccountEvent(AccountCreatedEvent accountCreatedEvent);

}
