package com.bank.management.values;

import com.bank.management.generic.Entity;
import com.bank.management.values.account.AccountId;
import com.bank.management.values.account.Amount;
import com.bank.management.values.generic.CreatedAt;
import com.bank.management.values.generic.IsDeleted;
import com.bank.management.values.account.Number;

import java.math.BigDecimal;
import java.util.List;

public class Account extends Entity<AccountId> {

    private AccountId id;
    private Customer customer;
    private Amount amount;
    private Number number;
    private IsDeleted isDeleted;
    private List<TransactionAccountDetail> transactionAccountDetails;
    private CreatedAt createdAt;

    public Account(AccountId id, Customer customer, Amount amount, Number number, IsDeleted isDeleted, List<TransactionAccountDetail> transactionAccountDetails, CreatedAt createdAt) {
        super(id);
        this.customer = customer;
        this.amount = amount;
        this.number = number;
        this.isDeleted = isDeleted;
        this.transactionAccountDetails = transactionAccountDetails;
        this.createdAt = createdAt;
    }

    public Account(AccountId id, Amount amount, Number number) {
        super(id);
        this.amount = amount;
        this.number = number;
    }

    private Account(Builder builder) {
        super(builder.id);
        this.id = builder.id;
        this.customer = builder.customer;
        this.amount = builder.amount;
        this.number = builder.number;
        this.isDeleted = builder.isDeleted;
        this.transactionAccountDetails = builder.transactionAccountDetails;
        this.createdAt = builder.createdAt;
    }

    public static class Builder {
        private AccountId id;
        private Customer customer;
        private Amount amount;
        private Number number;
        private IsDeleted isDeleted;
        private List<TransactionAccountDetail> transactionAccountDetails;
        private CreatedAt createdAt;

        public Builder id(AccountId id) {
            this.id = id;
            return this;
        }

        public Builder customer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder amount(Amount amount) {
            this.amount = amount;
            return this;
        }

        public Builder number(Number number) {
            this.number = number;
            return this;
        }

        public Builder isDeleted(IsDeleted isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public Builder transactionAccountDetails(List<TransactionAccountDetail> transactionAccountDetails) {
            this.transactionAccountDetails = transactionAccountDetails;
            return this;
        }

        public Builder createdAt(CreatedAt createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Account build() {
            return new Account(this);
        }
    }


    public void adjustBalance(BigDecimal amount) {
        this.amount = Amount.of(this.amount.value().add(amount));
    }

    public AccountId getId() {
        return id;
    }

    public void setId(AccountId id) {
        this.id = id;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public Number getNumber() {
        return number;
    }

    public void setNumber(Number number) {
        this.number = number;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public IsDeleted isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(IsDeleted isDeleted) {
        this.isDeleted = isDeleted;
    }

    public List<TransactionAccountDetail> getTransactionAccountDetails() {
        return transactionAccountDetails;
    }

    public void setTransactionAccountDetails(List<TransactionAccountDetail> transactionAccountDetails) {
        this.transactionAccountDetails = transactionAccountDetails;
    }

    public CreatedAt getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(CreatedAt createdAt) {
        this.createdAt = createdAt;
    }
}
