package com.bank.management.mapper;

import com.bank.management.values.Account;
import com.bank.management.values.Customer;
import com.bank.management.data.AccountDocument;
import com.bank.management.data.CustomerDocument;
import com.bank.management.values.customer.CustomerId;
import com.bank.management.values.customer.Lastname;
import com.bank.management.values.customer.Name;
import com.bank.management.values.customer.Username;
import com.bank.management.values.generic.CreatedAt;
import com.bank.management.values.generic.IsDeleted;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerMapper {

    public static CustomerDocument toDocument(Customer customer) {
        CustomerDocument document = new CustomerDocument();
        document.setId(customer.getId() != null ? customer.getId().value() : null);
        document.setUsername(customer.getUsername()!= null ? customer.getUsername().value() : null);
        document.setName(customer.getName() != null ? customer.getName().value() : null);
        document.setLastname(customer.getLastname() != null ? customer.getLastname().value() : null);
        document.setDeleted(customer.isDeleted() != null ? customer.isDeleted().value() : false);
        document.setCreatedAt(customer.getCreatedAt() != null ? customer.getCreatedAt().value() : null);

        if (customer.getAccounts() != null) {
            List<AccountDocument> accountDocuments = customer.getAccounts().stream()
                    .map(AccountMapper::toDocument)
                    .collect(Collectors.toList());
            document.setAccounts(accountDocuments);

            return document;
        }
        document.setAggregateRootId(customer.getId().value());
        return document;
    }

    public static Customer toDomain(CustomerDocument document) {

        Customer customer = new Customer.Builder().id(document.getId() != null ? CustomerId.of(document.getId()): null)
                .username(Username.of(document.getUsername()))
                .name(Name.of(document.getName()))
                .lastname(Lastname.of(document.getLastname()))
                .isDeleted(IsDeleted.of(document.isDeleted()))
                .createdAt(document.getCreatedAt() != null ? CreatedAt.of(document.getCreatedAt()) : CreatedAt.of(new Date()))
                .build();

            if (document.getAccounts() != null) {
                List<Account> accounts = document.getAccounts().stream()
                        .map(AccountMapper::toDomain)
                        .collect(Collectors.toList());
                customer.setAccounts(accounts);
            }

        return customer;
    }
}
