package com.bank.management.usecase.appservice;

import com.bank.management.command.ProcessDepositCommand;
import com.bank.management.exception.*;
import com.bank.management.gateway.*;
import com.bank.management.generic.Command;
import com.bank.management.generic.DomainEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;

public class ProcessDepositEventUseCase {

    private final ObjectMapper objectMapper;
    private final EventRepository eventRepository;
    private final EventBus eventBus;

    public ProcessDepositEventUseCase(ObjectMapper objectMapper, EventRepository eventRepository, EventBus eventBus) {
        this.objectMapper = objectMapper;
        this.eventRepository = eventRepository;
        this.eventBus = eventBus;
    }

    public Mono<DomainEvent>apply(ProcessDepositCommand command) {
        return Mono.empty();
    }

    public Mono<String> serializeCommand(Command command) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(command))
                .onErrorMap(e -> new RuntimeException("Error serializing command", e));
    }
}
