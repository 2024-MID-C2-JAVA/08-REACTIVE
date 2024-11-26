package co.sofka.appservice.eventsUseCase;

import co.sofka.commands.CreateAccountCommand;
import co.sofka.events.AccountCreatedEvent;
import co.sofka.events.DomainEvent;
import co.sofka.rabbitMq.CreateAccountEventUseCase;
import co.sofka.rabbitMq.EventRepository;
import co.sofka.rabbitMq.bus.EventBus;
import reactor.core.publisher.Mono;

public class CreateAccountCommandUseCaseImpl implements CreateAccountEventUseCase {

    private final EventBus accountEventBus;
    private final EventRepository eventRepository;

    public CreateAccountCommandUseCaseImpl(EventBus accountEventBus, EventRepository eventRepository) {
        this.accountEventBus = accountEventBus;
        this.eventRepository = eventRepository;
    }

    @Override
    public Mono<DomainEvent> publish(Mono<CreateAccountCommand> createAccountCommand) {
        return createAccountCommand.flatMap(command->{
            AccountCreatedEvent accountCreatedEvent=new AccountCreatedEvent();
            accountCreatedEvent.setAggregateRootId(command.getCustomerId());
            accountCreatedEvent.setAmount(command.getAmount());
            accountCreatedEvent.setNumber(command.getNumber());

            DomainEvent domainEvent=new AccountCreatedEvent();
            domainEvent.setAggregate(accountCreatedEvent.getAggregate());
            domainEvent.setVersionType(0L);
            domainEvent.setAggregateRootId(accountCreatedEvent.getAggregateRootId());
            domainEvent.setBody(createAccountCommand.toString());

            return eventRepository.save(domainEvent)
                    .flatMap(event-> accountEventBus.publishAccountEvent(accountCreatedEvent)
                            .thenReturn(event));
        });
    }
}
