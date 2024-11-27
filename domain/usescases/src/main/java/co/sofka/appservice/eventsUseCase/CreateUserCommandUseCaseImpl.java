package co.sofka.appservice.eventsUseCase;

import co.sofka.commands.CreateUserCommand;
import co.sofka.events.CreateUserEvent;
import co.sofka.rabbitMq.CreateUserEventUseCase;
import co.sofka.rabbitMq.EventRepository;
import co.sofka.rabbitMq.bus.EventBus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;

public class CreateUserCommandUseCaseImpl implements CreateUserEventUseCase {

    private final EventBus userEventBus;
    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper;

    public CreateUserCommandUseCaseImpl(EventBus userEventBus, EventRepository eventRepository, ObjectMapper objectMapper) {
        this.userEventBus = userEventBus;
        this.eventRepository = eventRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<CreateUserEvent> publish(Mono<CreateUserCommand> createUserCommandMono) {
        return createUserCommandMono.flatMap(command->{
            try {
                CreateUserEvent createUserEvent = new CreateUserEvent();
                createUserEvent.setFirstname(command.getFirstname());
                createUserEvent.setLastname(command.getLastname());
                createUserEvent.setEmail(command.getEmail());
                createUserEvent.setPassword(command.getPassword());
                createUserEvent.setRole(command.getRole());


                CreateUserEvent domainEvent=new CreateUserEvent();
                domainEvent.setAggregateRootId(command.getEmail());
                domainEvent.setVersionType(0L);
                domainEvent.setAggregate(objectMapper.writeValueAsString(command));

                return eventRepository.save(domainEvent)
                        .flatMap(event->userEventBus.publishUserEvent(createUserEvent)
                                .thenReturn(createUserEvent));
            } catch (JsonProcessingException e) {
                return Mono.error(new RuntimeException(e));
            }
        });
    }
}
