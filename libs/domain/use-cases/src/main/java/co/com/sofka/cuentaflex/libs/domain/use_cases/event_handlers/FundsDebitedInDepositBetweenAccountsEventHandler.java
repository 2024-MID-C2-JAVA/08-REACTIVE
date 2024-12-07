package co.com.sofka.cuentaflex.libs.domain.use_cases.event_handlers;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.TransactionType;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.events.FundsDebitedInDepositBetweenAccountsEvent;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.ViewRepositoryPort;
import co.com.sofka.cuentaflex.libs.domain.model.accounts_views.AccountRole;
import co.com.sofka.cuentaflex.libs.domain.model.accounts_views.AccountTransactionView;
import co.com.sofka.cuentaflex.libs.domain.model.accounts_views.TransactionView;
import co.com.sofka.cuentaflex.libs.domain.use_cases.ReactiveEventHandler;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public final class FundsDebitedInDepositBetweenAccountsEventHandler implements ReactiveEventHandler<FundsDebitedInDepositBetweenAccountsEvent> {
    private final ViewRepositoryPort viewRepositoryPort;

    public FundsDebitedInDepositBetweenAccountsEventHandler(ViewRepositoryPort viewRepositoryPort) {
        this.viewRepositoryPort = viewRepositoryPort;
    }

    @Override
    public Mono<Void> apply(FundsDebitedInDepositBetweenAccountsEvent fundsDebitedInDepositBetweenAccountsEvent) {
        return Mono.just(fundsDebitedInDepositBetweenAccountsEvent).map(
                event -> new AccountTransactionView(
                        new TransactionView(
                                event.getTransactionId(),
                                event.getAmount(),
                                event.getFee(),
                                TransactionType.DEPOSIT_BETWEEN_ACCOUNTS,
                                event.getWhen()
                        ),
                        AccountRole.PAYROLL
                )
        ).flatMap(accountTransactionView -> this.viewRepositoryPort.saveAccountTransactionToAccountView(
                        fundsDebitedInDepositBetweenAccountsEvent.getAggregateRootId(),
                        fundsDebitedInDepositBetweenAccountsEvent.getAccountId(),
                        accountTransactionView
                ).then(Mono.just(accountTransactionView))
        ).flatMap(_ -> this.viewRepositoryPort.getAccountView(
                        fundsDebitedInDepositBetweenAccountsEvent.getAggregateRootId(),
                        fundsDebitedInDepositBetweenAccountsEvent.getAccountId()
                )
        ).flatMap(accountView -> {
            BigDecimal newBalance = accountView.getBalance().subtract(fundsDebitedInDepositBetweenAccountsEvent.getAmount().add(fundsDebitedInDepositBetweenAccountsEvent.getFee()));
            return this.viewRepositoryPort.updateAccountBalance(
                    fundsDebitedInDepositBetweenAccountsEvent.getAggregateRootId(),
                    fundsDebitedInDepositBetweenAccountsEvent.getAccountId(),
                    newBalance
            );
        }).then();
    }
}
