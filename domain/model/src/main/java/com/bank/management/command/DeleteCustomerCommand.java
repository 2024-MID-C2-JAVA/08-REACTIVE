package com.bank.management.command;

import com.bank.management.generic.Command;

import java.util.Objects;

public class DeleteCustomerCommand extends Command {

    private String aggregateRootId;

    public DeleteCustomerCommand(String aggregateRootId) {
        this.aggregateRootId = Objects.requireNonNull(aggregateRootId, "aggregateRootId cannot be null");
    }

    public DeleteCustomerCommand() {
    }

    public String getAggregateRootId() {
        return aggregateRootId;
    }

    @Override
    public String toString() {
        return "DeleteCustomerCommand{" +
                "aggregateRootId='" + aggregateRootId + '\'' +
                '}';
    }
}
