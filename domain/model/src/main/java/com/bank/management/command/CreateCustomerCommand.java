package com.bank.management.command;

import com.bank.management.generic.Command;

import java.util.Objects;

public class CreateCustomerCommand extends Command {

    private final String aggregateRootId;
    private final String username;
    private final String name;
    private final String lastname;

    public CreateCustomerCommand(String aggregateRootId, String username, String name, String lastname) {
        this.aggregateRootId = Objects.requireNonNull(aggregateRootId, "AggregateRootId cannot be null");
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.lastname = Objects.requireNonNull(lastname, "Lastname cannot be null");
    }

    public String getAggregateRootId() {
        return aggregateRootId;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    @Override
    public String toString() {
        return "CreateCustomerCommand{" +
                "aggregateRootId='" + aggregateRootId + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }
}
