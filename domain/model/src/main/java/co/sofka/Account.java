package co.sofka;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Account {

    private String id;
    private int number;
    private BigDecimal amount;
    private String customerId;
    private LocalDate createdAt;
    private Boolean isDeleted;

    public Account(String id, int number, BigDecimal amount, String customerId, LocalDate createdAt, Boolean isDeleted) {
        this.id = id;
        this.number = number;
        this.amount = amount;
        this.customerId = customerId;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
    }

    public Account(String id, int number, BigDecimal amount, String customerId, LocalDate createdAt) {
        this.id = id;
        this.number = number;
        this.amount = amount;
        this.customerId = customerId;
        this.createdAt = createdAt;
    }

    public Account(String id) {
        this.id = id;
    }

    public Account(int number) {
        this.number = number;
    }

    public Account() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", number=" + number +
                ", amount=" + amount +
                ", customerId='" + customerId + '\'' +
                ", createdAt=" + createdAt +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
