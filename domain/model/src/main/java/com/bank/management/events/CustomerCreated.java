package com.bank.management.events;

import com.bank.management.generic.DomainEvent;
import com.bank.management.values.Account;

import java.util.Date;
import java.util.List;

public class CustomerCreated extends DomainEvent {

    private String id;
    private String username;
    private String name;
    private String lastname;
    private List<Account> accounts;
    private Boolean isDeleted;
    private Date createdAt;

    public CustomerCreated(String id, String username, String name, String lastname, Boolean isDeleted, Date created) {
        super("com.bank.management.events.CustomerCreated");
        this.id = id;
        this.username = username;
        this.name = name;
        this.lastname = lastname;
        this.isDeleted = isDeleted;
        this.createdAt = created;
    }

    public CustomerCreated() {
        super("com.bank.management.events.CustomerCreated");
    }

    public CustomerCreated(String id, String username, String name, String lastname, boolean isDeleted, Date createdAt) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.lastname = lastname;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
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

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
