package com.bank.management.command;

import com.bank.management.generic.Command;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Command object for processing a withdrawal.
 */
public class ProcessWithdrawalCommand extends Command {

    private String aggregateRootId;
    private String username;
    private String accountNumber;
    private BigDecimal amount;

    public ProcessWithdrawalCommand(String aggregateRootId, String username, String accountNumber, BigDecimal amount) {
        this.aggregateRootId = Objects.requireNonNull(aggregateRootId, "Aggregate Root ID cannot be null");
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.accountNumber = Objects.requireNonNull(accountNumber, "Account number cannot be null");
        this.amount = Objects.requireNonNull(amount, "Amount cannot be null");
    }

    public ProcessWithdrawalCommand() {
    }

    public void setAggregateRootId(String aggregateRootId) {
        this.aggregateRootId = aggregateRootId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAggregateRootId() {
        return aggregateRootId;
    }

    public String getUsername() {
        return username;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "ProcessWithdrawalCommand{" +
                "aggregateRootId='" + aggregateRootId + '\'' +
                ", username='" + username + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", amount=" + amount +
                '}';
    }
}
