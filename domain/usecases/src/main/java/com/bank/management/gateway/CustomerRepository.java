package com.bank.management.gateway;

import com.bank.management.customer.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerRepository {

    Flux<Customer> findAll();
    Mono<Customer> findById(String id);
    Mono<Customer> save(Customer customer);
    Mono<Customer> findByUsername(String username);
    Mono<Boolean> delete(Customer customer);
    Mono<Customer> findByNumber(String accountNumber);
}
