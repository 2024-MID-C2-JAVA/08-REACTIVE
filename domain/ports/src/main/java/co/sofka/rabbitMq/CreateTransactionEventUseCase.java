package co.sofka.rabbitMq;

import co.sofka.commands.CreateTransactionCommand;
import co.sofka.events.TransactionCreatedEvent;
import reactor.core.publisher.Mono;

public interface CreateTransactionEventUseCase {
    Mono<TransactionCreatedEvent> publish(Mono<CreateTransactionCommand> createTransactionCommand);
}
