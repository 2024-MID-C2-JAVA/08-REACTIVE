package com.bank.management.command;

import com.bank.management.generic.Command;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Command object for creating a bank account.
 */
public class CreateAccountCommand extends Command {

    private String aggregateRootId;
    private BigDecimal amount;
    private String accountId;

    public CreateAccountCommand(String aggregateRootId, String customerId, BigDecimal amount, String accountId) {
        this.aggregateRootId = Objects.requireNonNull(aggregateRootId, "Aggregate Root ID cannot be null");
        this.amount = Objects.requireNonNull(amount, "Amount cannot be null");
        this.accountId = Objects.requireNonNull(accountId, "Amount cannot be null");
    }

    public CreateAccountCommand() {
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAggregateRootId() {
        return aggregateRootId;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "CreateAccountCommand{" +
                "aggregateRootId='" + aggregateRootId + '\'' +
                ", amount=" + amount +
                '}';
    }
}
