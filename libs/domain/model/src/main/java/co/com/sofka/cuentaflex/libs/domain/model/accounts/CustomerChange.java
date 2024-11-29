package co.com.sofka.cuentaflex.libs.domain.model.accounts;

import co.com.sofka.cuentaflex.libs.domain.model.EventChange;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.events.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class CustomerChange extends EventChange {
    public CustomerChange(Customer customer) {
        this.apply((CustomerCreatedEvent customerCreatedEvent) -> {
            customer.createdAt = customerCreatedEvent.getWhen();
            customer.firstName = customerCreatedEvent.getFirstName();
            customer.lastName = customerCreatedEvent.getLastName();
            customer.identification = customerCreatedEvent.getIdentification();
            customer.accounts = new ArrayList<>();
            customer.isDeleted = false;
        });

        this.apply((AccountCreatedEvent accountCreatedEvent) -> {
            Account account = new Account(
                    AccountId.of(accountCreatedEvent.getAccountId()),
                    accountCreatedEvent.getAccountNumber(),
                    new Balance(accountCreatedEvent.getInitialBalance())
            );
            account.isDeleted = false;
            account.createdAt = accountCreatedEvent.getWhen();
            account.transactions = new ArrayList<>();
            customer.accounts.add(account);
        });

        this.apply((UnidirectionalTransactionPerformedEvent unidirectionalTransactionPerformedEvent) -> {
            customer.accounts.stream()
                    .filter(account -> account.getId().equals(AccountId.of(unidirectionalTransactionPerformedEvent.getAccountId())))
                    .findFirst()
                    .ifPresent(account -> {
                        account.transactions.add(new Transaction(
                                TransactionId.of(unidirectionalTransactionPerformedEvent.getTransactionId()),
                                unidirectionalTransactionPerformedEvent.getWhen(),
                                new Amount(unidirectionalTransactionPerformedEvent.getAmount()),
                                new Fee(unidirectionalTransactionPerformedEvent.getFee()),
                                unidirectionalTransactionPerformedEvent.getTransactionType()
                        ));

                        BigDecimal balanceAfterTransaction = unidirectionalTransactionPerformedEvent.getTransactionType().isDebit() ?
                                account.getBalance().getValue().subtract(unidirectionalTransactionPerformedEvent.getAmount().add(unidirectionalTransactionPerformedEvent.getFee())) :
                                account.getBalance().getValue().add(unidirectionalTransactionPerformedEvent.getAmount().subtract(unidirectionalTransactionPerformedEvent.getFee()));

                        account.balance = new Balance(balanceAfterTransaction);
                    });
        });

        this.apply((FundsCreditedInDepositBetweenAccountsEvent fundsCreditedInDepositBetweenAccountsEvent) -> {
            customer.accounts.stream()
                    .filter(account -> account.getId().equals(AccountId.of(fundsCreditedInDepositBetweenAccountsEvent.getAccountId())))
                    .findFirst()
                    .ifPresent(account -> {
                        account.transactions.add(new Transaction(
                                TransactionId.of(fundsCreditedInDepositBetweenAccountsEvent.getTransactionId()),
                                fundsCreditedInDepositBetweenAccountsEvent.getWhen(),
                                new Amount(fundsCreditedInDepositBetweenAccountsEvent.getAmount()),
                                new Fee(fundsCreditedInDepositBetweenAccountsEvent.getFee()),
                                TransactionType.DEPOSIT_BETWEEN_ACCOUNTS
                        ));

                        BigDecimal balanceAfterTransaction = account.getBalance().getValue().add(fundsCreditedInDepositBetweenAccountsEvent.getAmount());

                        account.balance = new Balance(balanceAfterTransaction);
                    });
        });

        this.apply((FundsDebitedInDepositBetweenAccountsEvent fundsDebitedInDepositBetweenAccountsEvent) -> {
            customer.accounts.stream()
                    .filter(account -> account.getId().equals(AccountId.of(fundsDebitedInDepositBetweenAccountsEvent.getAccountId())))
                    .findFirst()
                    .ifPresent(account -> {
                        account.transactions.add(new Transaction(
                                TransactionId.of(fundsDebitedInDepositBetweenAccountsEvent.getTransactionId()),
                                fundsDebitedInDepositBetweenAccountsEvent.getWhen(),
                                new Amount(fundsDebitedInDepositBetweenAccountsEvent.getAmount()),
                                new Fee(fundsDebitedInDepositBetweenAccountsEvent.getFee()),
                                TransactionType.DEPOSIT_BETWEEN_ACCOUNTS
                        ));

                        BigDecimal balanceAfterTransaction = account.getBalance().getValue().subtract(fundsDebitedInDepositBetweenAccountsEvent.getAmount().add(fundsDebitedInDepositBetweenAccountsEvent.getFee()));;

                        account.balance = new Balance(balanceAfterTransaction);
                    });
        });
    }
}
