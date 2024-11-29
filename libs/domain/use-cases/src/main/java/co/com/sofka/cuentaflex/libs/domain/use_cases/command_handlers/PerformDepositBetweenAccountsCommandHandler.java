package co.com.sofka.cuentaflex.libs.domain.use_cases.command_handlers;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.*;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.commands.PerformDepositBetweenAccountsCommand;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.messaging.EventBus;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.EventRepositoryPort;
import co.com.sofka.cuentaflex.libs.domain.use_cases.ReactiveCommandHandler;
import co.com.sofka.cuentaflex.libs.domain.use_cases.common.AccountTransactor;
import co.com.sofka.cuentaflex.libs.domain.use_cases.exceptions.CustomerDoesNotExistsException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class PerformDepositBetweenAccountsCommandHandler implements ReactiveCommandHandler<PerformDepositBetweenAccountsCommand> {
    private final EventRepositoryPort eventRepositoryPort;
    private final EventBus eventBus;
    private final AccountTransactor accountTransactor;

    public PerformDepositBetweenAccountsCommandHandler(EventRepositoryPort eventRepositoryPort, EventBus eventBus, AccountTransactor accountTransactor) {
        this.eventRepositoryPort = eventRepositoryPort;
        this.eventBus = eventBus;
        this.accountTransactor = accountTransactor;
    }

    @Override
    public Mono<Void> apply(PerformDepositBetweenAccountsCommand command) {
        Objects.requireNonNull(command, "The command cannot be null");

        return this.eventRepositoryPort.findByAggregateRootIdAndAggregateName(
                command.getToCustomerId(), Customer.class.getName()
        ).collectList().flatMap(events -> {
            if (events.isEmpty()) {
                return Mono.error(new CustomerDoesNotExistsException(command.getFromCustomerId()));
            }
            return Flux.fromIterable(events).collectList();
        }).flatMap(fromCustomerEvents -> {
            if (fromCustomerEvents.isEmpty()) {
                return Mono.error(new CustomerDoesNotExistsException(command.getFromCustomerId()));
            }
            Customer fromCustomer = Customer.from(CustomerId.of(command.getFromCustomerId()), fromCustomerEvents);

            return eventRepositoryPort.findByAggregateRootIdAndAggregateName(
                            command.getToCustomerId(),
                            Customer.class.getName()
                    ).collectList().flatMap(toCustomerEvents -> {
                        if (toCustomerEvents.isEmpty()) {
                            return Mono.error(new CustomerDoesNotExistsException(command.getToCustomerId()));
                        }
                        Customer toCustomer = Customer.from(CustomerId.of(command.getToCustomerId()), toCustomerEvents);

                        accountTransactor.applyDepositBetweenAccounts(
                                command.getTransactionId(),
                                fromCustomer,
                                AccountId.of(command.getFromAccountId()),
                                toCustomer,
                                AccountId.of(command.getToAccountId()),
                                new Amount(command.getAmount())
                        );

                        return Mono.just(Stream.concat(
                                fromCustomer.getUncommittedChanges().stream(),
                                toCustomer.getUncommittedChanges().stream()
                        ).collect(Collectors.toList()));
                    }).flatMapMany(Flux::fromIterable).map(event -> {
                        this.eventBus.publish(event);
                        return event;
                    })
                    .flatMap(this.eventRepositoryPort::save).then();
        }).then();
    }

}
