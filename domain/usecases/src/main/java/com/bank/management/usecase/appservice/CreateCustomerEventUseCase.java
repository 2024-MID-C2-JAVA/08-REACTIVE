package com.bank.management.usecase.appservice;

import com.bank.management.command.CreateCustomerCommand;
import com.bank.management.exception.UserAlreadyExistsException;
import com.bank.management.gateway.EventBus;
import com.bank.management.gateway.EventRepository;
import com.bank.management.generic.Command;
import com.bank.management.generic.DomainEvent;
import com.bank.management.values.Customer;
import com.bank.management.values.customer.CustomerId;
import com.bank.management.values.customer.Lastname;
import com.bank.management.values.customer.Name;
import com.bank.management.values.customer.Username;
import com.bank.management.values.generic.CreatedAt;
import com.bank.management.values.generic.IsDeleted;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;


public class CreateCustomerEventUseCase {

    private final EventRepository eventRepository;
    private final EventBus eventBus;
    private final ObjectMapper objectMapper;

    public CreateCustomerEventUseCase(EventRepository eventRepository, EventBus eventBus, ObjectMapper objectMapper) {
        this.eventRepository = eventRepository;
        this.eventBus = eventBus;
        this.objectMapper = objectMapper;
    }


    public Flux<DomainEvent> apply(Mono<CreateCustomerCommand> command) {
        return command.flatMapMany(cmd -> {
            Customer customer = new Customer(
                    CustomerId.of(cmd.getId()),
                    new Username(cmd.getUsername()),
                    new Name(cmd.getName()),
                    new Lastname(cmd.getLastname()),
                    new CreatedAt(new Date(System.currentTimeMillis())),
                    new IsDeleted(false)
            );

            return Flux.fromIterable(customer.getUncommittedChanges())
                    .flatMap(event ->
                            eventRepository.save(event)
                                    .doOnSuccess(eventBus::createCustomerEvent)
                    )
                    .doOnComplete(customer::markChangesAsCommitted);
        });
    }

    public Mono<String> serializeCommand(Command command) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(command))
                .onErrorMap(e -> new RuntimeException("Error serializing command", e));
    }
}
