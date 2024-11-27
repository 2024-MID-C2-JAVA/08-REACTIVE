package co.com.sofka.cuentaflex.libs.domain.model.accounts.commands;

import co.com.sofka.cuentaflex.libs.domain.model.Command;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.TransactionType;

import java.math.BigDecimal;

public final class PerformUnidirectionalTransactionCommand extends Command {
    private final String customerId;
    private final String accountId;
    private final String transactionId;
    private final BigDecimal amount;
    private final TransactionType transactionType;

    public PerformUnidirectionalTransactionCommand(String customerId, String accountId, String transactionId, BigDecimal amount, TransactionType transactionType) {
        this.customerId = customerId;
        this.accountId = accountId;
        this.transactionId = transactionId;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }
}
