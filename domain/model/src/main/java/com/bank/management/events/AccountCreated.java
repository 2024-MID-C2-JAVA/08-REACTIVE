package com.bank.management.events;

import com.bank.management.generic.DomainEvent;

import java.math.BigDecimal;

public class AccountCreated extends DomainEvent {

    private String accountId;
    private String number;
    private BigDecimal amount;

    public AccountCreated() {
        super("AccountCreated");
    }

    public AccountCreated(String accountId, BigDecimal amount, String number) {
        super("AccountCreated");
        this.number = number;
        this.accountId = accountId;

    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getNumber() {
        return number;
    }

    public String getAccountId() {
        return accountId;
    }
}
