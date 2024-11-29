package com.bank.management.values;

import com.bank.management.generic.AggregateRoot;
import com.bank.management.values.transaction.*;

public class Deposit extends AggregateRoot<TransactionId> {
    private TransactionId id;
    private Username username;
    private AccountNumber accountNumber;
    private Amount amount;
    private TypeTransaction typeTransaction;

    private Deposit(Builder builder) {
        super(builder.id);
        this.id = builder.id;
        this.username = builder.username;
        this.accountNumber = builder.accountNumber;
        this.amount = builder.amount;
        this.typeTransaction = builder.typeTransaction;
    }

    public Deposit(TransactionId id, Username username, AccountNumber accountNumber, Amount amount, TypeTransaction typeTransaction) {
        super(id);
        this.username = username;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.typeTransaction = typeTransaction;
    }

    // Getters


    public void setAccountNumber(AccountNumber accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public void setType(TypeTransaction typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public void setUsername(Username username) {
        this.username = username;
    }

    public void setId(TransactionId id) {
        this.id = id;
    }

    public TransactionId getId() {
        return id;
    }

    public Username getUsername() {
        return username;
    }

    public AccountNumber getAccountNumber() {
        return accountNumber;
    }

    public Amount getAmount() {
        return amount;
    }

    public TypeTransaction getType() {
        return typeTransaction;
    }

    public static class Builder {
        private TransactionId id;
        private Username username;
        private AccountNumber accountNumber;
        private Amount amount;
        private TypeTransaction typeTransaction;

        public Builder id(TransactionId id) {
            this.id = id;
            return this;
        }

        public Builder username(Username username) {
            this.username = username;
            return this;
        }

        public Builder accountNumber(AccountNumber accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public Builder amount(Amount amount) {
            this.amount = amount;
            return this;
        }

        public Builder type(TypeTransaction typeTransaction) {
            this.typeTransaction = typeTransaction;
            return this;
        }

        public Deposit build() {
            // Validaciones opcionales antes de construir
            if (username == null || accountNumber == null || amount == null || typeTransaction == null) {
                throw new IllegalStateException("All fields must be provided");
            }
            return new Deposit(this);
        }
    }

    @Override
    public String toString() {
        return "Deposit{" +
                "id=" + id +
                ", username=" + username +
                ", accountNumber=" + accountNumber +
                ", amount=" + amount +
                ", type=" + typeTransaction +
                '}';
    }
}
