package co.sofka.appservice.eventsUseCase;

import co.sofka.commands.CreateAccountCommand;
import co.sofka.events.AccountCreatedEvent;
import co.sofka.events.DomainEvent;
import co.sofka.rabbitMq.CreateAccountEventUseCase;
import co.sofka.rabbitMq.EventRepository;
import co.sofka.rabbitMq.bus.EventBus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;

public class CreateAccountCommandUseCaseImpl implements CreateAccountEventUseCase {

    private final EventBus accountEventBus;
    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper;

    public CreateAccountCommandUseCaseImpl(EventBus accountEventBus, EventRepository eventRepository, ObjectMapper objectMapper) {
        this.accountEventBus = accountEventBus;
        this.eventRepository = eventRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<DomainEvent> publish(Mono<CreateAccountCommand> createAccountCommand) {
        return createAccountCommand.flatMap(command->{
            try {
                AccountCreatedEvent accountCreatedEvent=new AccountCreatedEvent();
                accountCreatedEvent.setAggregateRootId(command.getCustomerId());
                accountCreatedEvent.setAmount(command.getAmount());
                accountCreatedEvent.setNumber(command.getNumber());

                DomainEvent domainEvent=new AccountCreatedEvent();
                domainEvent.setVersionType(0L);
                domainEvent.setAggregateRootId(accountCreatedEvent.getAggregateRootId());
                domainEvent.setAggregate(objectMapper.writeValueAsString(command));

                System.out.println("Estoy en el caso de uso");

                return eventRepository.save(domainEvent)
                        .flatMap(event-> accountEventBus.publishAccountEvent(accountCreatedEvent)
                                .thenReturn(event));
            } catch (JsonProcessingException e) {
                return Mono.error(new RuntimeException(e));
            }
        });
    }
}
