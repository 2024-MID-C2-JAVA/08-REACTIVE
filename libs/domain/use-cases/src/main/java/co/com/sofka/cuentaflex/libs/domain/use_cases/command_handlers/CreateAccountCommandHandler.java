package co.com.sofka.cuentaflex.libs.domain.use_cases.command_handlers;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.*;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.commands.CreateAccountCommand;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.messaging.EventBus;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.EventRepositoryPort;
import co.com.sofka.cuentaflex.libs.domain.use_cases.ReactiveCommandHandler;
import co.com.sofka.cuentaflex.libs.domain.use_cases.exceptions.CustomerDoesNotExistsException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public final class CreateAccountCommandHandler implements ReactiveCommandHandler<CreateAccountCommand> {
    private final EventRepositoryPort eventRepositoryPort;
    private final EventBus eventBus;

    public CreateAccountCommandHandler(EventRepositoryPort eventRepositoryPort, EventBus eventBus) {
        this.eventRepositoryPort = eventRepositoryPort;
        this.eventBus = eventBus;
    }

    @Override
    public Mono<Void> apply(CreateAccountCommand createAccountCommand) {
        return this.eventRepositoryPort.findByAggregateRootIdAndAggregateName(
                createAccountCommand.getCustomerId(), Customer.class.getName()
        ).collectList().flatMap(events -> {
            if (events.isEmpty()) {
                return Mono.error(new CustomerDoesNotExistsException(createAccountCommand.getCustomerId()));
            }
            return Flux.fromIterable(events).collectList();
        }).flatMapIterable(events -> {
            Customer customer = Customer.from(CustomerId.of(createAccountCommand.getCustomerId()), events);
            Account account = new Account(
                    AccountId.of(createAccountCommand.getAccountId()),
                    createAccountCommand.getAccountNumber(),
                    new Balance(createAccountCommand.getInitialBalance())
            );
            customer.addAccount(account);
            return customer.getUncommittedChanges();
        }).map(event -> {
            this.eventBus.publish(event);
            return event;
        }).flatMap(this.eventRepositoryPort::save).then();
    }
}
