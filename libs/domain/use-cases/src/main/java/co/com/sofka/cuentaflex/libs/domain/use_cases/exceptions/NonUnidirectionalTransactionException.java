package co.com.sofka.cuentaflex.libs.domain.use_cases.exceptions;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.TransactionType;

public final class NonUnidirectionalTransactionException extends RuntimeException {
    private final TransactionType transactionType;

    public NonUnidirectionalTransactionException(TransactionType transactionType) {
        super(transactionType.name() + " is not an unidirectional transaction.");

        this.transactionType = transactionType;
    }

    public TransactionType getTransactionType() {
        return this.transactionType;
    }
}
