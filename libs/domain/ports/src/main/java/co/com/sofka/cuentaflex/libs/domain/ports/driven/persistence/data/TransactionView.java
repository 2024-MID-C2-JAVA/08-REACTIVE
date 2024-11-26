package co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.data;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionView {
    private String transactionId;
    private String accountId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private LocalDateTime timestamp;

    public TransactionView() {
    }

    public TransactionView(String transactionId, String accountId, BigDecimal amount, TransactionType transactionType, LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.timestamp = timestamp;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
