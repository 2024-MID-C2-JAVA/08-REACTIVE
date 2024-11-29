package co.sofka.appservice.eventsUseCase;

import co.sofka.commands.CreateTransactionCommand;
import co.sofka.events.DomainEvent;
import co.sofka.events.TransactionCreatedEvent;
import co.sofka.rabbitMq.CreateTransactionEventUseCase;
import co.sofka.rabbitMq.EventRepository;
import co.sofka.rabbitMq.bus.EventBus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;

public class CreateTransactionCommandUseCaseImpl implements CreateTransactionEventUseCase {

    private final EventRepository eventRepository;
    private final EventBus transactionEventBus;
    private final ObjectMapper objectMapper;

    public CreateTransactionCommandUseCaseImpl(EventRepository eventRepository, EventBus transactionEventBus, ObjectMapper objectMapper) {
        this.eventRepository = eventRepository;
        this.transactionEventBus = transactionEventBus;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<TransactionCreatedEvent> publish(Mono<CreateTransactionCommand> createTransactionCommand) {
        return createTransactionCommand
                .flatMap(command->{
                    try {

                        TransactionCreatedEvent transactionCreatedEvent=new TransactionCreatedEvent();
                        transactionCreatedEvent.setAggregateRootId(command.getAccountId());
                        transactionCreatedEvent.setAmount(command.getAmount());
                        transactionCreatedEvent.setCustomerId(command.getAccountId());
                        transactionCreatedEvent.setType(command.getType());

                        DomainEvent domainEvent=new TransactionCreatedEvent();
                        domainEvent.setAggregateRootId(command.getAccountId());
                        domainEvent.setVersionType(0L);
                        domainEvent.setAggregate(objectMapper.writeValueAsString(command));
                        return eventRepository
                                .save(domainEvent)
                                .flatMap(eventSaved->transactionEventBus.publishTransactionEvent(transactionCreatedEvent));
                    } catch (JsonProcessingException e) {
                        return Mono.error(new RuntimeException(e));
                    }
                });
    }
}
