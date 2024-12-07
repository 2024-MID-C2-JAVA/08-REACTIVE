package co.com.sofka.cuentaflex.libs.domain.use_cases.common;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.*;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.FeeProvider;
import co.com.sofka.cuentaflex.libs.domain.use_cases.exceptions.AccountDoesNotExistsException;
import co.com.sofka.cuentaflex.libs.domain.use_cases.exceptions.NonUnidirectionalTransactionException;

import java.time.LocalDateTime;
import java.util.Objects;

public final class AccountTransactor {
    private final FeeProvider feeProvider;

    public AccountTransactor(FeeProvider feeProvider) {
        this.feeProvider = feeProvider;
    }

    public Transaction applyUnidirectionalTransactionToAccount(
            String transactionId,
            Customer customer,
            AccountId accountId,
            Amount amount,
            TransactionType type
    ) {
        Objects.requireNonNull(customer, "The customer cannot be null");
        Objects.requireNonNull(accountId, "The account cannot be null");
        Objects.requireNonNull(amount, "The amount cannot be null");
        Objects.requireNonNull(type, "The transaction type cannot be null");

        Account account = customer.getAccounts().stream().filter(
                a -> a.getId().equals(accountId)
        ).findFirst().orElseThrow(
                () -> new AccountDoesNotExistsException(
                        accountId.getValue()
                )
        );

        if (!type.isUnidirectional()) {
            throw new NonUnidirectionalTransactionException(type);
        }

        Fee fee = feeProvider.getFeeForTransactionType(type);

        if(type.isDebit()) {
            account.debit(amount.addFee(fee));
        } else {
            account.credit(amount.subtractFee(fee));
        }

        Transaction transaction = new Transaction(
                TransactionId.of(transactionId),
                LocalDateTime.now(),
                amount,
                fee,
                type
        );

        customer.addTransactionToAccount(accountId, transaction);

        return transaction;
    }

    public Transaction applyDepositBetweenAccounts(
            String transactionId,
            Customer fromCustomer,
            AccountId fromAccountId,
            Customer toCustomer,
            AccountId toAccountId,
            Amount amount
    ) {
        Objects.requireNonNull(fromCustomer, "The from customer cannot be null");
        Objects.requireNonNull(fromAccountId, "The from account id cannot be null");
        Objects.requireNonNull(toAccountId, "The to account id cannot be null");
        Objects.requireNonNull(amount, "The amount cannot be null");

        Fee fee = feeProvider.getFeeForTransactionType(TransactionType.DEPOSIT_BETWEEN_ACCOUNTS);
        Amount amountToDebit = amount.addFee(fee);

        Account fromAccount = fromCustomer.getAccounts().stream().filter(
                a -> a.getId().equals(fromAccountId)
        ).findFirst().orElseThrow(
                () -> new AccountDoesNotExistsException(
                        fromAccountId.getValue()
                )
        );

        Account toAccount = toCustomer.getAccounts().stream().filter(
                a -> a.getId().equals(toAccountId)
        ).findFirst().orElseThrow(
                () -> new AccountDoesNotExistsException(
                        toAccountId.getValue()
                )
        );

        fromAccount.debit(amountToDebit);
        toAccount.credit(amount);

        Transaction transaction = new Transaction(
                TransactionId.of(transactionId),
                LocalDateTime.now(),
                amount,
                fee,
                TransactionType.DEPOSIT_BETWEEN_ACCOUNTS
        );

        fromCustomer.addDebitTransactionBetweenAccounts(fromAccountId, transaction);
        toCustomer.addCreditTransactionBetweenAccounts(toAccountId, transaction);

        return transaction;
    }
}
