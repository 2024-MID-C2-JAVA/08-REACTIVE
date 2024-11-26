package co.sofka.appservice.eventsUseCase;

import co.sofka.commands.CreateTransactionCommand;
import co.sofka.events.DomainEvent;
import co.sofka.events.TransactionCreatedEvent;
import co.sofka.rabbitMq.CreateTransactionEventUseCase;
import co.sofka.rabbitMq.EventRepository;
import co.sofka.rabbitMq.bus.EventBus;
import reactor.core.publisher.Mono;

public class CreateTransactionCommandUseCaseImpl implements CreateTransactionEventUseCase {

    private final EventRepository eventRepository;
    private final EventBus transactionEventBus;

    public CreateTransactionCommandUseCaseImpl(EventRepository eventRepository, EventBus transactionEventBus) {
        this.eventRepository = eventRepository;
        this.transactionEventBus = transactionEventBus;
    }

    @Override
    public Mono<TransactionCreatedEvent> publish(Mono<CreateTransactionCommand> createTransactionCommand) {
        return createTransactionCommand
                .flatMap(command->{
                    TransactionCreatedEvent transactionCreatedEvent=new TransactionCreatedEvent();
                    transactionCreatedEvent.setAggregateRootId(command.getAccountId());
                    transactionCreatedEvent.setAmount(command.getAmount());
                    transactionCreatedEvent.setCustomerId(command.getAccountId());
                    transactionCreatedEvent.setType(command.getType());

                    DomainEvent domainEvent=new TransactionCreatedEvent();
                    domainEvent.setAggregateRootId(command.getAccountId());
                    domainEvent.setAggregate(transactionCreatedEvent.getAggregate());
                    domainEvent.setVersionType(0L);
                    domainEvent.setBody(command.toString());

                    return eventRepository
                            .save(domainEvent)
                            .flatMap(eventSaved->transactionEventBus.publishTransactionEvent(transactionCreatedEvent));
                });
    }
}
