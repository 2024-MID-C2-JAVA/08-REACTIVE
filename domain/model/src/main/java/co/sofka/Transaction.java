package co.sofka;

import co.sofka.enums.PayRole;
import co.sofka.enums.TypeOfTransaction;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class Transaction {

    private String id;
    private BigDecimal amount;
    private BigDecimal amountCost;
    private TypeOfTransaction type;
    private OffsetDateTime timestamp;
    private String accountId;
    private PayRole payRole;

    public Transaction(String id, BigDecimal amount, BigDecimal amountCost, TypeOfTransaction type, OffsetDateTime timestamp) {
        this.id = id;
        this.amount = amount;
        this.amountCost = amountCost;
        this.type = type;
        this.timestamp = timestamp;
    }

    public Transaction() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountCost() {
        return amountCost;
    }

    public void setAmountCost(BigDecimal amountCost) {
        this.amountCost = amountCost;
    }

    public TypeOfTransaction getType() {
        return type;
    }

    public void setType(TypeOfTransaction type) {
        this.type = type;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public PayRole getPayRole() {
        return payRole;
    }

    public void setPayRole(PayRole payRole) {
        this.payRole = payRole;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", amountCost=" + amountCost +
                ", type=" + type +
                ", timestamp=" + timestamp +
                ", accountId='" + accountId + '\'' +
                ", payRole=" + payRole +
                '}';
    }
}
