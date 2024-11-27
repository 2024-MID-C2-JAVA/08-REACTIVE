package co.com.sofka.cuentaflex.libs.domain.use_cases.command_handlers;

import co.com.sofka.cuentaflex.libs.domain.model.DomainEvent;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.*;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.commands.PerformUnidirectionalTransactionCommand;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.events.*;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.messaging.EventBus;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.EventRepositoryPort;
import co.com.sofka.cuentaflex.libs.domain.use_cases.ReactiveCommandHandler;
import co.com.sofka.cuentaflex.libs.domain.use_cases.common.AccountTransactor;
import co.com.sofka.cuentaflex.libs.domain.use_cases.common.CustomerReconstructor;
import co.com.sofka.cuentaflex.libs.domain.use_cases.exceptions.AccountDoesNotExistsException;
import co.com.sofka.cuentaflex.libs.domain.use_cases.exceptions.CustomerDoesNotExistsException;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

public class PerformUnidirectionalTransactionCommandHandler implements ReactiveCommandHandler<PerformUnidirectionalTransactionCommand> {
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

        return eventRepositoryPort.findByAggregateRootIdAndAggregateName(performUnidirectionalTransactionCommand.getCustomerId(), Customer.class.getName())
                .collectList()
                .flatMap(customerEvents -> {
                    if (customerEvents.isEmpty()) {
                        return Mono.error(new CustomerDoesNotExistsException(performUnidirectionalTransactionCommand.getCustomerId()));
                    }
                    return Mono.just(CustomerReconstructor.reconstructCustomerFromEvents(customerEvents));
                }).flatMap(customer -> {
                    Optional<Account> accountOptional = customer.getAccounts().stream()
                            .filter(account -> account.getId().equals(performUnidirectionalTransactionCommand.getAccountId()))
                            .findFirst();

                    return accountOptional.map(Mono::just)
                            .orElseGet(
                                    () -> Mono.error(new AccountDoesNotExistsException(performUnidirectionalTransactionCommand.getAccountId()))
                            );
                })
                .flatMap(account -> {
                    try {
                        Transaction transaction = accountTransactor.applyUnidirectionalTransactionToAccount(
                                performUnidirectionalTransactionCommand.getTransactionId(),
                                account,
                                new Amount(performUnidirectionalTransactionCommand.getAmount()),
                                performUnidirectionalTransactionCommand.getTransactionType()
                        );
                        return Mono.just(transaction);
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                })
                .flatMap(transaction -> {
                    UnidirectionalTransactionPerformedEvent event = new UnidirectionalTransactionPerformedEvent(
                            performUnidirectionalTransactionCommand.getCustomerId(),
                            performUnidirectionalTransactionCommand.getAccountId(),
                            performUnidirectionalTransactionCommand.getTransactionId(),
                            transaction.getAmount().getValue(),
                            transaction.getFee().getValue(),
                            performUnidirectionalTransactionCommand.getTransactionType()
                    );
                    return Mono.just(event);
                }).map(event -> {
                    this.eventBus.publish(event);
                    return event;
                }).flatMap(this.eventRepositoryPort::save).then();
    }
}
