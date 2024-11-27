package co.com.sofka.cuentaflex.libs.domain.use_cases.common;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.*;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.FeeProvider;

import java.time.LocalDateTime;
import java.util.Objects;

public final class AccountTransactor {
    private final FeeProvider feeProvider;

    public AccountTransactor(FeeProvider feeProvider) {
        this.feeProvider = feeProvider;
    }

    public Transaction applyUnidirectionalTransactionToAccount(String transactionId, Account account, Amount amount, TransactionType type) {
        Objects.requireNonNull(account, "The account cannot be null");
        Objects.requireNonNull(amount, "The amount cannot be null");
        Objects.requireNonNull(type, "The transaction type cannot be null");

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
                transactionId,
                LocalDateTime.now(),
                amount,
                fee,
                type
        );

        account.addTransaction(transaction);

        return transaction;
    }

    public Transaction applyDepositBetweenAccounts(String transactionId, Account fromAccount, Account toAccount, Amount amount) {
        Objects.requireNonNull(fromAccount, "The from account cannot be null");
        Objects.requireNonNull(toAccount, "The to account cannot be null");
        Objects.requireNonNull(amount, "The amount cannot be null");

        Fee fee = feeProvider.getFeeForTransactionType(TransactionType.DEPOSIT_BETWEEN_ACCOUNTS);
        Amount amountToDebit = amount.addFee(fee);

        fromAccount.debit(amountToDebit);
        toAccount.credit(amount);

        Transaction transaction = new Transaction(
                transactionId,
                LocalDateTime.now(),
                amount,
                fee,
                TransactionType.DEPOSIT_BETWEEN_ACCOUNTS
        );

        fromAccount.addTransaction(transaction);
        toAccount.addTransaction(transaction);

        return transaction;
    }
}
