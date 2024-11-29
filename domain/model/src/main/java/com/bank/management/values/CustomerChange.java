package com.bank.management.values;

import com.bank.management.events.AccountCreated;
import com.bank.management.events.CustomerCreated;
import com.bank.management.generic.EventChange;
import com.bank.management.values.account.AccountId;
import com.bank.management.values.account.Amount;
import com.bank.management.values.account.Number;
import com.bank.management.values.customer.Lastname;
import com.bank.management.values.customer.Name;
import com.bank.management.values.customer.Username;
import com.bank.management.values.generic.CreatedAt;
import com.bank.management.values.generic.IsDeleted;

import java.util.ArrayList;

public class CustomerChange extends EventChange {

    public CustomerChange(Customer customer) {
        apply((CustomerCreated event) -> {
            customer.name = new Name(event.getName());
            customer.username = new Username(event.getUsername());
            customer.lastname = new Lastname(event.getLastname());
            customer.accounts = new ArrayList<>();
            customer.isDeleted = new IsDeleted(event.getDeleted());
            customer.createdAt = new CreatedAt(event.getCreatedAt());
        });

        apply((AccountCreated event) -> {
            Account account = new Account(
                    AccountId.of(event.getAccountId()),
                    Amount.of(event.getAmount()),
                    Number.of(event.getNumber()));
            customer.accounts.add(account);
        });
    }
}
