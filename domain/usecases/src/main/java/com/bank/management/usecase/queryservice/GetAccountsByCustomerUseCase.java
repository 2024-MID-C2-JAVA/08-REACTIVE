package com.bank.management.usecase.queryservice;

import com.bank.management.values.Account;
import com.bank.management.gateway.AccountRepository;
import reactor.core.publisher.Flux;

public class GetAccountsByCustomerUseCase {

    private final AccountRepository bankAccountRepository;

    public GetAccountsByCustomerUseCase(AccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public Flux<Account> apply(String id) {
        return bankAccountRepository.findByCustomerId(id);
    }

}