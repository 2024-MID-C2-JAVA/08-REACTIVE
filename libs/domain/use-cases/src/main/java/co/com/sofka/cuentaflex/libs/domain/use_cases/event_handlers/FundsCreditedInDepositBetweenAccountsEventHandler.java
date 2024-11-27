package co.com.sofka.cuentaflex.libs.domain.use_cases.event_handlers;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.TransactionType;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.events.FundsCreditedInDepositBetweenAccountsEvent;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.ViewRepositoryPort;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.data.AccountRole;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.data.AccountTransactionView;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.data.TransactionView;
import co.com.sofka.cuentaflex.libs.domain.use_cases.ReactiveEventHandler;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class FundsCreditedInDepositBetweenAccountsEventHandler implements ReactiveEventHandler<FundsCreditedInDepositBetweenAccountsEvent> {
    private final ViewRepositoryPort viewRepositoryPort;

    public FundsCreditedInDepositBetweenAccountsEventHandler(ViewRepositoryPort viewRepositoryPort) {
        this.viewRepositoryPort = viewRepositoryPort;
    }

    @Override
    public Mono<Void> apply(FundsCreditedInDepositBetweenAccountsEvent fundsCreditedInDepositBetweenAccountsEvent) {
        return Mono.just(fundsCreditedInDepositBetweenAccountsEvent).map(
                event -> new AccountTransactionView(
                        new TransactionView(
                                event.getTransactionId(),
                                event.getAmount(),
                                event.getFee(),
                                TransactionType.DEPOSIT_BETWEEN_ACCOUNTS,
                                LocalDateTime.ofInstant(event.getWhen(), ZoneOffset.UTC)
                        ),
                        AccountRole.SUPPLIER
                )
        ).flatMap(accountTransactionView -> this.viewRepositoryPort.saveAccountTransactionToAccountView(
                        fundsCreditedInDepositBetweenAccountsEvent.getAggregateRootId(),
                        fundsCreditedInDepositBetweenAccountsEvent.getAccountId(),
                        accountTransactionView
                ).then(Mono.just(accountTransactionView))
        ).flatMap(_ -> this.viewRepositoryPort.getAccountView(
                        fundsCreditedInDepositBetweenAccountsEvent.getAggregateRootId(),
                        fundsCreditedInDepositBetweenAccountsEvent.getAccountId()
                )
        ).flatMap(accountView -> {
            BigDecimal newBalance = accountView.getBalance().add(fundsCreditedInDepositBetweenAccountsEvent.getAmount());
            return this.viewRepositoryPort.updateAccountBalance(
                    fundsCreditedInDepositBetweenAccountsEvent.getAggregateRootId(),
                    fundsCreditedInDepositBetweenAccountsEvent.getAccountId(),
                    newBalance
            );
        }).then();
    }
}
