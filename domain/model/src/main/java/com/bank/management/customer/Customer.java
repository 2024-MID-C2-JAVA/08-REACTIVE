package com.bank.management.customer;

import java.util.Date;
import java.util.List;

public class Customer {

    private String id;
    private String username;
    private String name;
    private String lastname;
    private List<Account> accounts;
    private boolean isDeleted;
    private Date createdAt;

    private Customer(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.accounts = builder.accounts;
        this.name = builder.name;
        this.lastname = builder.lastname;
        this.isDeleted = builder.isDeleted;
        this.createdAt = builder.createdAt != null ? builder.createdAt : new Date();
    }

    // Getters y Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date created_at) {
        this.createdAt = createdAt;
    }

    public static class Builder {
        private String id;
        private String username;
        private String name;
        private String lastname;
        private List<Account> accounts;
        private Date createdAt;
        private boolean isDeleted;

        public Builder() {}

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder isDeleted(boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        public Builder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder accounts(List<Account> accounts) {
            this.accounts = accounts;
            return this;
        }

        public Builder createdAt(Date created_at) {
            this.createdAt = created_at;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}
