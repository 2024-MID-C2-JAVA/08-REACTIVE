package com.bank.management.usecase.appservice;

import com.bank.management.command.CreateAccountCommand;
import com.bank.management.exception.UserAlreadyExistsException;
import com.bank.management.gateway.EventBus;
import com.bank.management.gateway.EventRepository;
import com.bank.management.generic.Command;
import com.bank.management.generic.DomainEvent;
import com.bank.management.values.Customer;
import com.bank.management.values.account.AccountId;
import com.bank.management.values.account.Amount;
import com.bank.management.values.account.Number;
import com.bank.management.values.customer.CustomerId;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class CreateBankAccountEventUseCase {

    private final EventRepository eventRepository;
    private final EventBus eventBus;
    private final ObjectMapper objectMapper;

    public CreateBankAccountEventUseCase(EventRepository eventRepository, EventBus eventBus, ObjectMapper objectMapper) {
        this.eventRepository = eventRepository;
        this.eventBus = eventBus;
        this.objectMapper = objectMapper;
    }

    public Flux<DomainEvent> apply(Mono<CreateAccountCommand> command) {
        return command.flatMapMany(cmd ->
                eventRepository.findById(cmd.getAggregateRootId())
                        .collectList()
                        .flatMapMany(events -> {
                            Customer customer = Customer.from(CustomerId.of(cmd.getAggregateRootId()), events);

                            customer.addAccount(
                                    AccountId.of(cmd.getAccountId()),
                                    Amount.of(cmd.getAmount()),
                                    Number.of(generateAccountNumber())
                            );

                            return Flux.fromIterable(customer.getUncommittedChanges())
                                    .flatMap(event ->
                                            eventRepository.save(event)
                                                    .doOnSuccess(eventBus::createAccountEvent)
                                    )
                                    .doOnComplete(customer::markChangesAsCommitted);
                        })
        );
    }

    public Mono<String> serializeCommand(Command command) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(command))
                .onErrorMap(e -> new RuntimeException("Error serializing command", e));
    }

    private String generateAccountNumber() {
        return IntStream.range(0, 10)
                .mapToObj(i -> String.valueOf(new Random().nextInt(10)))
                .collect(Collectors.joining());
    }
}
