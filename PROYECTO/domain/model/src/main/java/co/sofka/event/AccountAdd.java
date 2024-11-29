package co.sofka.event;

import co.sofka.generic.DomainEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountAdd extends DomainEvent {

    private String accountId;

    private String number;

    private BigDecimal amount;

    private LocalDateTime createdAt;

    private Boolean isDeleted;


    public AccountAdd() {
        super("CreateAccount");
    }

    public AccountAdd(String accountId,String number, BigDecimal amount, LocalDateTime createdAt, Boolean isDeleted) {
        super("CreateAccount");
        this.accountId = accountId;
        this.number = number;
        this.amount = amount;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
