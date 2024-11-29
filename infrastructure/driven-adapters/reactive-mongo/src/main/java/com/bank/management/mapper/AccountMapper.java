package com.bank.management.mapper;

import com.bank.management.values.Account;
import com.bank.management.values.Customer;
import com.bank.management.data.AccountDocument;
import com.bank.management.values.account.AccountId;
import com.bank.management.values.account.Amount;
import com.bank.management.values.account.Number;
import com.bank.management.values.customer.CustomerId;
import com.bank.management.values.generic.CreatedAt;
import com.bank.management.values.generic.IsDeleted;

import java.util.Date;

public class AccountMapper {

    public static AccountDocument toDocument(Account account) {
        AccountDocument document = new AccountDocument();
        document.setId(account.getId() != null ? account.getId().value() : null);
        document.setNumber(account.getNumber() != null ? account.getNumber().value() : null);
        document.setAmount(account.getNumber() != null ? account.getAmount().value() : null);
        document.setCustomerId(account.getCustomer() != null ? account.getCustomer().getId().value() : null);
        document.setDeleted(account.isDeleted() != null ? account.isDeleted().value() : false);
        document.setCreatedAt(account.getCreatedAt() != null ? account.getCreatedAt().value() : null);
        return document;
    }

    public static Account toDomain(AccountDocument document) {
        Customer customer = new Customer.Builder().id(CustomerId.of(document.getCustomerId())).build();
        return new Account.Builder()
                .id(AccountId.of(document.getId()))
                .number(Number.of(document.getNumber()))
                .amount(Amount.of(document.getAmount()))
                .customer(customer)
                .isDeleted(IsDeleted.of(document.isDeleted()))
                .createdAt(document.getCreatedAt() != null ? CreatedAt.of(document.getCreatedAt()) : CreatedAt.of(new Date()))
                .build();
    }
}
