package com.bank.management.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Document(collection = "customer")
public class CustomerDocument {

    @Id
    private String id;

    private String aggregateRootId;
    private String username;
    private String name;
    private String lastname;

    @Field("user")
    private UserDocument user;

    @Field("accounts")
    private List<AccountDocument> accounts;

    @Field("created_at")
    private Date createdAt;

    @Field("is_deleted")
    private boolean isDeleted;

    public CustomerDocument() {}

    public CustomerDocument(String username, List<AccountDocument> accounts, UserDocument user, String agregateRootId) {
        this.aggregateRootId = agregateRootId;
        this.username = username;
        this.accounts = accounts;
        this.user = user;
        this.createdAt = new Date();
    }

    // Getters y Setters


    public String getAggregateRootId() {
        return aggregateRootId;
    }

    public void setAggregateRootId(String aggregateRootId) {
        this.aggregateRootId = aggregateRootId;
    }

    public UserDocument getUser() {
        return user;
    }

    public void setUser(UserDocument user) {
        this.user = user;
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

    public List<AccountDocument> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountDocument> accountIds) {
        this.accounts = accountIds;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
