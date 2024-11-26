package com.bank.management.command;

import com.bank.management.generic.Command;

import java.util.Objects;

public class DeleteBankAccountCommand extends Command {

    private String aggregateRootId;

    public DeleteBankAccountCommand(String aggregateRootId) {
        this.aggregateRootId = Objects.requireNonNull(aggregateRootId, "aggregateRootId cannot be null");
    }

    public DeleteBankAccountCommand() {
    }

    public String getAggregateRootId() {
        return aggregateRootId;
    }

    @Override
    public String toString() {
        return "DeleteBankAccountCommand{" +
                "aggregateRootId='" + aggregateRootId + '\'' +
                '}';
    }
}
