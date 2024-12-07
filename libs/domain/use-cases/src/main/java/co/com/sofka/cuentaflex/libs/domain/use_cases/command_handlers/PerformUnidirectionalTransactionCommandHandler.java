package co.com.sofka.cuentaflex.libs.domain.use_cases.command_handlers;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.*;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.commands.PerformUnidirectionalTransactionCommand;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.messaging.EventBus;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.EventRepositoryPort;
import co.com.sofka.cuentaflex.libs.domain.use_cases.ReactiveCommandHandler;
import co.com.sofka.cuentaflex.libs.domain.use_cases.common.AccountTransactor;
import co.com.sofka.cuentaflex.libs.domain.use_cases.exceptions.CustomerDoesNotExistsException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

public final class PerformUnidirectionalTransactionCommandHandler implements ReactiveCommandHandler<PerformUnidirectionalTransactionCommand> {
    private final EventRepositoryPort eventRepositoryPort;
    private final EventBus eventBus;
    private final AccountTransactor accountTransactor;

    public PerformUnidirectionalTransactionCommandHandler(EventRepositoryPort eventRepositoryPort, EventBus eventBus, AccountTransactor accountTransactor) {
        this.eventRepositoryPort = eventRepositoryPort;
        this.eventBus = eventBus;
        this.accountTransactor = accountTransactor;
    }

    @Override
    public Mono<Void> apply(PerformUnidirectionalTransactionCommand performUnidirectionalTransactionCommand) {
        Objects.requireNonNull(performUnidirectionalTransactionCommand, "The command cannot be null");

        return this.eventRepositoryPort.findByAggregateRootIdAndAggregateName(
                performUnidirectionalTransactionCommand.getCustomerId(), Customer.class.getName()
        ).collectList().flatMap(events -> {
            if (events.isEmpty()) {
                return Mono.error(new CustomerDoesNotExistsException(performUnidirectionalTransactionCommand.getCustomerId()));
            }
            return Flux.fromIterable(events).collectList();
        }).flatMap(events -> {
            Customer customer = Customer.from(CustomerId.of(performUnidirectionalTransactionCommand.getCustomerId()), events);

            try {
                Transaction transaction = accountTransactor.applyUnidirectionalTransactionToAccount(
                        performUnidirectionalTransactionCommand.getTransactionId(),
                        customer,
                        AccountId.of(performUnidirectionalTransactionCommand.getAccountId()),
                        new Amount(performUnidirectionalTransactionCommand.getAmount()),
                        performUnidirectionalTransactionCommand.getTransactionType()
                );
                return Flux.fromIterable(customer.getUncommittedChanges()).collectList();
            } catch (Exception e) {
                return Mono.error(e);
            }
        }).flatMapMany(
                Flux::fromIterable
        ).map(event -> {
            this.eventBus.publish(event);
            return event;
        }).flatMap(this.eventRepositoryPort::save).then();
    }
}
