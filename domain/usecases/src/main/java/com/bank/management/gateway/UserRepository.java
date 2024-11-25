package com.bank.management.gateway;

import com.bank.management.customer.Customer;
import com.bank.management.customer.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> saveUserAndCustomer(User user, Customer customer);
    Mono<User> findByUsername(String username);
}
