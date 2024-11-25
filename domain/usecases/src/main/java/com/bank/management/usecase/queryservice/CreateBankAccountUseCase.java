package com.bank.management.usecase.queryservice;

import com.bank.management.customer.Account;
import com.bank.management.customer.Customer;
import com.bank.management.exception.AccountCreationException;
import com.bank.management.exception.CustomerNotFoundException;
import com.bank.management.gateway.AccountRepository;
import com.bank.management.gateway.CustomerRepository;
import reactor.core.publisher.Mono;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class CreateBankAccountUseCase {

    private final AccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;

    public CreateBankAccountUseCase(AccountRepository bankAccountRepository, CustomerRepository customerRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.customerRepository = customerRepository;
    }


    public Mono<Account>  apply(Account account, Customer customer) {
        return customerRepository.findById(customer.getId())
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found with ID: " + customer.getId())))
                .flatMap(customerFound -> {
                    Account accountToCreate = new Account.Builder()
                            .number(generateAccountNumber())
                            .amount(account.getAmount())
                            .build();

                    accountToCreate.setCustomer(customerFound);

                    return bankAccountRepository.save(accountToCreate)
                            .switchIfEmpty(Mono.error(new AccountCreationException("Failed to create account for customer: " + customer.getId())));
                });
    }

    private String generateAccountNumber() {
        return IntStream.range(0, 10)
                .mapToObj(i -> String.valueOf(new Random().nextInt(10)))
                .collect(Collectors.joining());
    }
}
