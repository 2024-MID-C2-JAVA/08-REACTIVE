package co.com.sofka.cuentaflex.libs.domain.use_cases.event_handlers;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.events.AccountCreatedEvent;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.ViewRepositoryPort;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.data.AccountView;
import co.com.sofka.cuentaflex.libs.domain.use_cases.ReactiveEventHandler;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

public final class AccountCreatedEventHandler implements ReactiveEventHandler<AccountCreatedEvent> {
    private final ViewRepositoryPort viewRepositoryPort;

    public AccountCreatedEventHandler(ViewRepositoryPort viewRepositoryPort) {
        this.viewRepositoryPort = viewRepositoryPort;
    }


    @Override
    public Mono<Void> apply(AccountCreatedEvent accountCreatedEvent) {
        Objects.requireNonNull(accountCreatedEvent, "The account created event cannot be null");

        return Mono.just(accountCreatedEvent)
                .map(_ -> new AccountView(
                        accountCreatedEvent.getAccountId(),
                        accountCreatedEvent.getAccountNumber(),
                        accountCreatedEvent.getInitialBalance(),
                        false,
                        LocalDateTime.now(),
                        List.of()
                ))
                .flatMap(account -> viewRepositoryPort.saveAccountToCustomerView(accountCreatedEvent.getAggregateRootId(), account));
    }
}
