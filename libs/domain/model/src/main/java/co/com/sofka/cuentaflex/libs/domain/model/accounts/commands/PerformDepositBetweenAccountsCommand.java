package co.com.sofka.cuentaflex.libs.domain.model.accounts.commands;

import co.com.sofka.cuentaflex.libs.domain.model.Command;

import java.math.BigDecimal;

public final class PerformDepositBetweenAccountsCommand extends Command {
    private final String fromCustomerId;
    private final String fromAccountId;
    private final String toCustomerId;
    private final String toAccountId;
    private final String transactionId;
    private final BigDecimal amount;

    public PerformDepositBetweenAccountsCommand(String fromCustomerId, String fromAccountId, String toCustomerId, String toAccountId, String transactionId, BigDecimal amount) {
        this.fromCustomerId = fromCustomerId;
        this.fromAccountId = fromAccountId;
        this.toCustomerId = toCustomerId;
        this.toAccountId = toAccountId;
        this.transactionId = transactionId;
        this.amount = amount;
    }

    public String getFromCustomerId() {
        return fromCustomerId;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public String getToCustomerId() {
        return toCustomerId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
