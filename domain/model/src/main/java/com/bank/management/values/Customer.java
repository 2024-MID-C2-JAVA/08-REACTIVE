package com.bank.management.values;

import com.bank.management.events.AccountCreated;
import com.bank.management.events.CustomerCreated;
import com.bank.management.generic.AggregateRoot;
import com.bank.management.generic.DomainEvent;
import com.bank.management.values.account.AccountId;
import com.bank.management.values.account.Amount;
import com.bank.management.values.account.Number;
import com.bank.management.values.customer.*;
import com.bank.management.values.generic.CreatedAt;
import com.bank.management.values.generic.IsDeleted;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Customer extends AggregateRoot<CustomerId> {

    protected CustomerId id;
    protected Username username;
    protected Name name;
    protected Lastname lastname;
    protected List<Account> accounts;
    protected IsDeleted isDeleted;
    protected CreatedAt createdAt;


    public Customer(CustomerId id) {
        super(id);
        subscribe(new CustomerChange(this));
    }

    public Customer(CustomerId id, Username username, Name name, Lastname lastname, CreatedAt createdAt, IsDeleted isDeleted) {
        super(id);
        subscribe(new CustomerChange(this));
        appendChange(new CustomerCreated(id.value(), username.value(), name.value(),
        lastname.value(), isDeleted.value(), createdAt.value())).apply();
    }

    public static Customer from(CustomerId id, List<DomainEvent> events) {
        Customer customer = new Customer(id);
        events.forEach(customer::applyEvent);
        return customer;
    }

    public void addAccount(AccountId accountId, Amount amount, Number number) {
        Objects.requireNonNull(accountId, "Entity id cannot be null");
        Objects.requireNonNull(amount, "Author cannot be null");
        Objects.requireNonNull(number, "Content cannot be null");
        appendChange(new AccountCreated(accountId.value(), amount.value(), number.value()));
    }

    private Customer(Builder builder) {
        super(builder.id);
        this.id = builder.id;
        this.username = builder.username;
        this.name = builder.name;
        this.lastname = builder.lastname;
        this.accounts = builder.accounts;
        this.isDeleted = builder.isDeleted;
        this.createdAt = builder.createdAt;
        subscribe(new CustomerChange(this));
    }

    public static class Builder {
        private CustomerId id;
        private Username username;
        private Name name;
        private Lastname lastname;
        private List<Account> accounts;
        private IsDeleted isDeleted;
        private CreatedAt createdAt;

        public Builder id(CustomerId id) {
            this.id = id;
            return this;
        }

        public Builder username(Username username) {
            this.username = username;
            return this;
        }

        public Builder name(Name name) {
            this.name = name;
            return this;
        }

        public Builder lastname(Lastname lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder accounts(List<Account> accounts) {
            this.accounts = accounts;
            return this;
        }

        public Builder isDeleted(IsDeleted isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public Builder createdAt(CreatedAt createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }

    public CustomerId getId() {
        return id;
    }

    public void setId(CustomerId id) {
        this.id = id;
    }

    public Username getUsername() {
        return username;
    }

    public void setUsername(Username username) {
        this.username = username;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public IsDeleted isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(IsDeleted isDeleted) {
        this.isDeleted = isDeleted;
    }

    public CreatedAt getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(CreatedAt createdAt) {
        this.createdAt = createdAt;
    }

    public Lastname getLastname() {
        return lastname;
    }

    public void setLastname(Lastname lastname) {
        this.lastname = lastname;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }
}
