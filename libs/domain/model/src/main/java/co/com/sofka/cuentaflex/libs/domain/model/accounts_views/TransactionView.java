package co.com.sofka.cuentaflex.libs.domain.model.accounts_views;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionView {
    private String transactionId;
    private BigDecimal amount;
    private BigDecimal fee;
    private TransactionType transactionType;
    private LocalDateTime timestamp;

    public TransactionView() {
    }

    public TransactionView(String transactionId, BigDecimal amount, BigDecimal fee, TransactionType transactionType, LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.fee = fee;
        this.transactionType = transactionType;
        this.timestamp = timestamp;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
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
