package com.bank.management.usecase.queryservice;

import com.bank.management.values.Account;
import com.bank.management.exception.BankAccountNotFoundException;
import com.bank.management.gateway.AccountRepository;
import reactor.core.publisher.Mono;


public class GetBankAccountUseCase {

    private final AccountRepository bankAccountRepository;

    public GetBankAccountUseCase(AccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public Mono<Account> apply(String id) {
        return bankAccountRepository.findById(id)
                .switchIfEmpty(Mono.error(new BankAccountNotFoundException()));
    }

}
