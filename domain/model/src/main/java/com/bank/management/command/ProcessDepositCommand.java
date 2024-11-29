package com.bank.management.command;

import com.bank.management.generic.Command;

import java.math.BigDecimal;
import java.util.Objects;

public class ProcessDepositCommand extends Command {

    private String aggregateRootId;
    private String username;
    private String accountNumber;
    private BigDecimal amount;
    private String type; // (e.g., "PHYSICAL", "ONLINE")

    public ProcessDepositCommand(String aggregateRootId, String username, String accountNumber, BigDecimal amount, String type) {
        this.aggregateRootId = Objects.requireNonNull(aggregateRootId, "Aggregate Root ID cannot be null");
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.accountNumber = Objects.requireNonNull(accountNumber, "Account number cannot be null");
        this.amount = Objects.requireNonNull(amount, "Amount cannot be null");
        this.type = Objects.requireNonNull(type, "Transaction type cannot be null");
    }

    public void setAggregateRootId(String aggregateRootId) {
        this.aggregateRootId = aggregateRootId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ProcessDepositCommand() {
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

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ProcessDepositCommand{" +
                "aggregateRootId='" + aggregateRootId + '\'' +
                ", username='" + username + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                '}';
    }
}
