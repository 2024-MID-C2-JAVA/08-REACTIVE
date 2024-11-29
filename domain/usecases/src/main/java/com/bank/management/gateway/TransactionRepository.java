package com.bank.management.gateway;

import com.bank.management.values.Account;
import com.bank.management.values.Customer;
import com.bank.management.values.Transaction;
import reactor.core.publisher.Mono;


public interface TransactionRepository {
    Mono<Transaction> save(Transaction trx, Account account, Customer customer, String role);
}
