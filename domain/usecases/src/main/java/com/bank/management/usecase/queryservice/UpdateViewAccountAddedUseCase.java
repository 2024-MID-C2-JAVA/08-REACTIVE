package com.bank.management.usecase.queryservice;

import com.bank.management.values.Account;
import com.bank.management.values.Customer;
import com.bank.management.exception.AccountCreationException;
import com.bank.management.exception.CustomerNotFoundException;
import com.bank.management.gateway.AccountRepository;
import com.bank.management.gateway.CustomerRepository;
import reactor.core.publisher.Mono;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class UpdateViewAccountAddedUseCase {

    private final AccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;

    public UpdateViewAccountAddedUseCase(AccountRepository bankAccountRepository, CustomerRepository customerRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.customerRepository = customerRepository;
    }


    public Mono<Account>  apply(Account account, Customer customer) {
        return customerRepository.findById(customer.getId().value())
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found with ID: " + customer.getId())))
                .flatMap(customerFound -> {
                    Account accountToCreate = new Account.Builder()
                            .number(account.getNumber())
                            .amount(account.getAmount())
                            .build();

                    accountToCreate.setCustomer(customerFound);

                    return bankAccountRepository.save(accountToCreate)
                            .switchIfEmpty(Mono.error(new AccountCreationException("Failed to create account for customer: " + customer.getId())));
                });
    }

}
