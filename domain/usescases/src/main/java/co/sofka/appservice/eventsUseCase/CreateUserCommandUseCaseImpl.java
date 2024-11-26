package co.sofka.appservice.eventsUseCase;

import co.sofka.commands.CreateUserCommand;
import co.sofka.events.CreateUserEvent;
import co.sofka.events.DomainEvent;
import co.sofka.rabbitMq.CreateUserEventUseCase;
import co.sofka.rabbitMq.EventRepository;
import co.sofka.rabbitMq.bus.EventBus;
import reactor.core.publisher.Mono;

public class CreateUserCommandUseCaseImpl implements CreateUserEventUseCase {

    private final EventBus userEventBus;
    private final EventRepository eventRepository;

    public CreateUserCommandUseCaseImpl(EventBus userEventBus, EventRepository eventRepository) {
        this.userEventBus = userEventBus;
        this.eventRepository = eventRepository;
    }

    @Override
    public Mono<CreateUserEvent> publish(Mono<CreateUserCommand> createUserCommandMono) {
        return createUserCommandMono.flatMap(command->{
            CreateUserEvent createUserEvent = new CreateUserEvent();
            createUserEvent.setFirstname(command.getFirstname());
            createUserEvent.setLastname(command.getLastname());
            createUserEvent.setEmail(command.getEmail());
            createUserEvent.setPassword(command.getPassword());
            createUserEvent.setRole(command.getRole());


            DomainEvent domainEvent=new CreateUserEvent();
            domainEvent.setAggregate(createUserEvent.getAggregate());
            domainEvent.setAggregateRootId(command.getEmail());
            domainEvent.setVersionType(0L);
            domainEvent.setBody(command.toString());

            return eventRepository.save(domainEvent)
                    .flatMap(event->userEventBus.publishUserEvent(createUserEvent)
                            .thenReturn(createUserEvent));
        });
    }
}
