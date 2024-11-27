package co.com.sofka.cuentaflex.libs.domain.use_cases.command_handlers;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.Account;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.Amount;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.Customer;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.Transaction;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.commands.PerformDepositBetweenAccountsCommand;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.events.FundsCreditedInDepositBetweenAccountsEvent;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.events.FundsDebitedInDepositBetweenAccountsEvent;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.messaging.EventBus;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.EventRepositoryPort;
import co.com.sofka.cuentaflex.libs.domain.use_cases.ReactiveCommandHandler;
import co.com.sofka.cuentaflex.libs.domain.use_cases.common.AccountTransactor;
import co.com.sofka.cuentaflex.libs.domain.use_cases.common.CustomerReconstructor;
import co.com.sofka.cuentaflex.libs.domain.use_cases.exceptions.AccountDoesNotExistsException;
import co.com.sofka.cuentaflex.libs.domain.use_cases.exceptions.CustomerDoesNotExistsException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PerformDepositBetweenAccountsCommandHandler implements ReactiveCommandHandler<PerformDepositBetweenAccountsCommand> {
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

        return eventRepositoryPort.findByAggregateRootIdAndAggregateName(command.getFromCustomerId(), Customer.class.getName())
                .collectList()
                .flatMap(fromCustomerEvents -> {
                    if (fromCustomerEvents.isEmpty()) {
                        return Mono.error(new CustomerDoesNotExistsException(command.getFromCustomerId()));
                    }
                    Customer fromCustomer = CustomerReconstructor.reconstructCustomerFromEvents(fromCustomerEvents);

                    return eventRepositoryPort.findByAggregateRootIdAndAggregateName(command.getToCustomerId(), Customer.class.getName())
                            .collectList()
                            .flatMap(toCustomerEvents -> {
                                if (toCustomerEvents.isEmpty()) {
                                    return Mono.error(new CustomerDoesNotExistsException(command.getToCustomerId()));
                                }
                                Customer toCustomer = CustomerReconstructor.reconstructCustomerFromEvents(toCustomerEvents);

                                Optional<Account> fromAccountOptional = fromCustomer.getAccounts().stream()
                                        .filter(account -> account.getId().equals(command.getFromAccountId()))
                                        .findFirst();

                                Optional<Account> toAccountOptional = toCustomer.getAccounts().stream()
                                        .filter(account -> account.getId().equals(command.getToAccountId()))
                                        .findFirst();

                                if (fromAccountOptional.isEmpty()) {
                                    return Mono.error(new AccountDoesNotExistsException(command.getFromAccountId()));
                                }

                                if (toAccountOptional.isEmpty()) {
                                    return Mono.error(new AccountDoesNotExistsException(command.getToAccountId()));
                                }

                                Account fromAccount = fromAccountOptional.get();
                                Account toAccount = toAccountOptional.get();

                                Transaction transaction = accountTransactor.applyDepositBetweenAccounts(
                                        command.getTransactionId(),
                                        fromAccount,
                                        toAccount,
                                        new Amount(command.getAmount())
                                );

                                FundsDebitedInDepositBetweenAccountsEvent debitEvent = new FundsDebitedInDepositBetweenAccountsEvent(
                                        command.getFromCustomerId(),
                                        command.getFromAccountId(),
                                        command.getTransactionId(),
                                        transaction.getAmount().getValue(),
                                        transaction.getFee().getValue()
                                );

                                FundsCreditedInDepositBetweenAccountsEvent creditEvent = new FundsCreditedInDepositBetweenAccountsEvent(
                                        command.getToCustomerId(),
                                        command.getToAccountId(),
                                        command.getTransactionId(),
                                        transaction.getAmount().getValue(),
                                        transaction.getFee().getValue()
                                );

                                return Mono.just(List.of(debitEvent, creditEvent));
                            }).flatMapMany(events -> {
                                events.forEach(eventBus::publish);
                                return Flux.fromIterable(events);
                            })
                            .flatMap(this.eventRepositoryPort::save)
                            .then();
                });
    }

}
