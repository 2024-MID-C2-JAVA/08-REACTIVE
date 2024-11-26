package com.bank.management.usecase.appservice;

import com.bank.management.command.CreateAccountCommand;
import com.bank.management.command.CreateCustomerCommand;
import com.bank.management.command.DeleteCustomerCommand;
import com.bank.management.exception.UserAlreadyExistsException;
import com.bank.management.gateway.EventBus;
import com.bank.management.gateway.EventRepository;
import com.bank.management.generic.Command;
import com.bank.management.generic.DomainEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;


public class DeleteCustomerEventUseCase {

    private final EventRepository eventRepository;
    private final EventBus eventBus;
    private final ObjectMapper objectMapper;

    public DeleteCustomerEventUseCase(EventRepository eventRepository, EventBus eventBus, ObjectMapper objectMapper) {
        this.eventRepository = eventRepository;
        this.eventBus = eventBus;
        this.objectMapper = objectMapper;
    }


    public Mono<DomainEvent>apply(DeleteCustomerCommand command) {
        return serializeCommand(command)
                .map(serializedCommand -> {
                    DomainEvent event = new DomainEvent("DeleteCreatedEvent", serializedCommand);
                    event.setAggregateRootId(command.getAggregateRootId());
                    return event;
                })
                .flatMap(event -> {
                    eventBus.deleteCustomerEvent(event);
                    return eventRepository.save(event);
                })
                .onErrorResume(UserAlreadyExistsException.class, Mono::error)
                .onErrorResume(Exception.class, Mono::error);
    }

    public Mono<String> serializeCommand(Command command) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(command))
                .onErrorMap(e -> new RuntimeException("Error serializing command", e));
    }
}
