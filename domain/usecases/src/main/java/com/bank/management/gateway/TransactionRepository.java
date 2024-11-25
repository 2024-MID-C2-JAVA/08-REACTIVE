package com.bank.management.gateway;

import com.bank.management.customer.Account;
import com.bank.management.customer.Customer;
import com.bank.management.transaction.Transaction;
import reactor.core.publisher.Mono;


public interface TransactionRepository {
    Mono<Transaction> save(Transaction trx, Account account, Customer customer, String role);
}
