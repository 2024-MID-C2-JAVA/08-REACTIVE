package com.bank.management.gateway;

import com.bank.management.values.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepository {
    Mono<Account> findById(String id);
    Mono<Account> save(Account account);
    Mono<Boolean> delete(String id);
    Mono<Account> findByNumber(String accountNumber);
    Flux<Account> findByCustomerId(String id);
}