package com.bank.management.command;

import com.bank.management.generic.Command;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Command object for processing a purchase.
 */
public class ProcessPurchaseCommand extends Command {

    private String aggregateRootId;
    private String accountNumber;
    private BigDecimal amount;
    private String purchaseType; // "PHYSICAL" or "ONLINE"

    public ProcessPurchaseCommand(String aggregateRootId, String accountNumber, BigDecimal amount, String type) {
        this.aggregateRootId = Objects.requireNonNull(aggregateRootId, "Aggregate Root ID cannot be null");
        this.accountNumber = Objects.requireNonNull(accountNumber, "Account number cannot be null");
        this.amount = Objects.requireNonNull(amount, "Amount cannot be null");
        purchaseType = Objects.requireNonNull(type, "Type cannot be null");
    }

    public ProcessPurchaseCommand() {
    }

    public String getAggregateRootId() {
        return aggregateRootId;
    }

    public void setAggregateRootId(String aggregateRootId) {
        this.aggregateRootId = aggregateRootId;
    }

    public String getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(String type) {
        this.purchaseType = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public String toString() {
        return "ProcessPurchaseCommand{" +
                "aggregateRootId='" + aggregateRootId + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", amount=" + amount +
                ", type='" + purchaseType + '\'' +
                '}';
    }
}
