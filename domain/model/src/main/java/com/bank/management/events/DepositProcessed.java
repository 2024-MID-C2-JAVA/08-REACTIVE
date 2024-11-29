package com.bank.management.events;

import com.bank.management.generic.DomainEvent;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class DepositProcessed  extends DomainEvent {

    private String transactionId;
    private String username;
    private String accountNumber;
    private BigDecimal amount;
    private String typeTransaction;

    public DepositProcessed(String transactionId, String username, BigDecimal amount, String type, String accountNumber) {
        super("Deposit");
        this.transactionId = transactionId;
        this.username = username;
        this.amount = amount;
        this.typeTransaction = type;
        this.accountNumber = accountNumber;
    }

    public DepositProcessed() {
        super("Deposit");
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setTypeTransaction(String typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public String getTypeTransaction() {
        return typeTransaction;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
