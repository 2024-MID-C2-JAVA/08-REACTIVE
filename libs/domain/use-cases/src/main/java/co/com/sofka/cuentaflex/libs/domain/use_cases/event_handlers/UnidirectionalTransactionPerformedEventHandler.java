package co.com.sofka.cuentaflex.libs.domain.use_cases.event_handlers;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.events.UnidirectionalTransactionPerformedEvent;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.ViewRepositoryPort;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.data.AccountRole;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.data.AccountTransactionView;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.data.TransactionView;
import co.com.sofka.cuentaflex.libs.domain.use_cases.ReactiveEventHandler;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class UnidirectionalTransactionPerformedEventHandler implements ReactiveEventHandler<UnidirectionalTransactionPerformedEvent> {
    private final ViewRepositoryPort viewRepositoryPort;

    public UnidirectionalTransactionPerformedEventHandler(ViewRepositoryPort viewRepositoryPort) {
        this.viewRepositoryPort = viewRepositoryPort;
    }

    @Override
    public Mono<Void> apply(UnidirectionalTransactionPerformedEvent unidirectionalTransactionPerformedEvent) {
        return Mono.just(
                unidirectionalTransactionPerformedEvent
        ).map(event -> new AccountTransactionView(
                        new TransactionView(
                                event.getTransactionId(),
                                event.getAmount(),
                                event.getFee(),
                                event.getTransactionType(),
                                LocalDateTime.ofInstant(event.getWhen(), ZoneOffset.UTC)
                        ),
                        event.getTransactionType().isDebit() ? AccountRole.PAYROLL : AccountRole.SUPPLIER
                )
        ).flatMap(
                accountTransactionView -> this.viewRepositoryPort.saveAccountTransactionToAccountView(
                        unidirectionalTransactionPerformedEvent.getAggregateRootId(),
                        unidirectionalTransactionPerformedEvent.getAccountId(),
                        accountTransactionView
                ).then(Mono.just(accountTransactionView))
        ).flatMap(_ -> this.viewRepositoryPort.getAccountView(
                        unidirectionalTransactionPerformedEvent.getAggregateRootId(),
                        unidirectionalTransactionPerformedEvent.getAccountId()
                )
        ).flatMap(accountView -> {
                    BigDecimal newBalance = unidirectionalTransactionPerformedEvent.getTransactionType().isDebit() ?
                            accountView.getBalance().subtract(unidirectionalTransactionPerformedEvent.getAmount().add(unidirectionalTransactionPerformedEvent.getFee())) :
                            accountView.getBalance().add(unidirectionalTransactionPerformedEvent.getAmount().subtract(unidirectionalTransactionPerformedEvent.getFee()));

                    return this.viewRepositoryPort.updateAccountBalance(
                            unidirectionalTransactionPerformedEvent.getAggregateRootId(),
                            unidirectionalTransactionPerformedEvent.getAccountId(),
                            newBalance
                    );
                }
        ).then();
    }
}
