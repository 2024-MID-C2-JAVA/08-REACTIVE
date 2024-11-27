package co.com.sofka.cuentaflex.libs.domain.use_cases.common;

import co.com.sofka.cuentaflex.libs.domain.model.DomainEvent;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.*;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.events.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class CustomerReconstructor {
    public static Customer reconstructCustomerFromEvents(List<DomainEvent> customerEvents) {
        Customer customer = new Customer(null, null, null, null, null, null, true);
        customerEvents.forEach(event -> performCustomerEvent(customer, event));
        return customer;
    }

    private static void performCustomerEvent(Customer customer, DomainEvent event) {
        if (event instanceof CustomerCreatedEvent customerCreatedEvent) {
            customer.setId(customerCreatedEvent.getAggregateRootId());
            customer.setCreatedAt(LocalDateTime.ofInstant(customerCreatedEvent.getWhen(), ZoneOffset.UTC));
            customer.setFirstName(customerCreatedEvent.getFirstName());
            customer.setLastName(customerCreatedEvent.getLastName());
            customer.setIdentification(customerCreatedEvent.getIdentification());
            customer.setAccounts(new ArrayList<>());
            customer.setDeleted(false);
            return;
        }

        if (event instanceof AccountCreatedEvent accountCreatedEvent) {
            customer.addAccount(
                    new Account(
                            accountCreatedEvent.getAccountId(),
                            LocalDateTime.ofInstant(accountCreatedEvent.getWhen(), ZoneOffset.UTC),
                            0,
                            new Balance(accountCreatedEvent.getInitialBalance()),
                            new ArrayList<>(),
                            false
                    )
            );
            return;
        }

        if (event instanceof UnidirectionalTransactionPerformedEvent unidirectionalTransactionPerformedEvent) {
            customer.getAccounts().stream()
                    .filter(account -> account.getId().equals(unidirectionalTransactionPerformedEvent.getAccountId()))
                    .findFirst()
                    .ifPresent(account -> {
                        account.addTransaction(new Transaction(
                                unidirectionalTransactionPerformedEvent.getTransactionId(),
                                LocalDateTime.ofInstant(unidirectionalTransactionPerformedEvent.getWhen(), ZoneOffset.UTC),
                                new Amount(unidirectionalTransactionPerformedEvent.getAmount()),
                                new Fee(unidirectionalTransactionPerformedEvent.getFee()),
                                unidirectionalTransactionPerformedEvent.getTransactionType()
                        ));

                        BigDecimal balanceAfterTransaction = unidirectionalTransactionPerformedEvent.getTransactionType().isDebit() ?
                                account.getBalance().getValue().subtract(unidirectionalTransactionPerformedEvent.getAmount().add(unidirectionalTransactionPerformedEvent.getFee())) :
                                account.getBalance().getValue().add(unidirectionalTransactionPerformedEvent.getAmount().subtract(unidirectionalTransactionPerformedEvent.getFee()));

                        account.setBalance(new Balance(balanceAfterTransaction));
                    });
        }

        if (event instanceof FundsCreditedInDepositBetweenAccountsEvent fundsCreditedInDepositBetweenAccountsEvent) {
            customer.getAccounts().stream()
                    .filter(account -> account.getId().equals(fundsCreditedInDepositBetweenAccountsEvent.getAccountId()))
                    .findFirst()
                    .ifPresent(account -> {
                        account.addTransaction(new Transaction(
                                fundsCreditedInDepositBetweenAccountsEvent.getTransactionId(),
                                LocalDateTime.ofInstant(fundsCreditedInDepositBetweenAccountsEvent.getWhen(), ZoneOffset.UTC),
                                new Amount(fundsCreditedInDepositBetweenAccountsEvent.getAmount()),
                                new Fee(fundsCreditedInDepositBetweenAccountsEvent.getFee()),
                                TransactionType.DEPOSIT_BETWEEN_ACCOUNTS
                        ));

                        BigDecimal balanceAfterTransaction = account.getBalance().getValue().add(fundsCreditedInDepositBetweenAccountsEvent.getAmount());

                        account.setBalance(new Balance(balanceAfterTransaction));
                    });
        }

        if (event instanceof FundsDebitedInDepositBetweenAccountsEvent fundsDebitedInDepositBetweenAccountsEvent) {
            customer.getAccounts().stream()
                    .filter(account -> account.getId().equals(fundsDebitedInDepositBetweenAccountsEvent.getAccountId()))
                    .findFirst()
                    .ifPresent(account -> {
                        account.addTransaction(new Transaction(
                                fundsDebitedInDepositBetweenAccountsEvent.getTransactionId(),
                                LocalDateTime.ofInstant(fundsDebitedInDepositBetweenAccountsEvent.getWhen(), ZoneOffset.UTC),
                                new Amount(fundsDebitedInDepositBetweenAccountsEvent.getAmount()),
                                new Fee(fundsDebitedInDepositBetweenAccountsEvent.getFee()),
                                TransactionType.DEPOSIT_BETWEEN_ACCOUNTS
                        ));

                        BigDecimal balanceAfterTransaction = account.getBalance().getValue().subtract(fundsDebitedInDepositBetweenAccountsEvent.getAmount().add(fundsDebitedInDepositBetweenAccountsEvent.getFee()));

                        account.setBalance(new Balance(balanceAfterTransaction));
                    });
        }
    }
}
