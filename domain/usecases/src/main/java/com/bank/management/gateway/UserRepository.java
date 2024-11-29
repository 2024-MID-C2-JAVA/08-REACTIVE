package com.bank.management.gateway;

import com.bank.management.values.Customer;
import com.bank.management.values.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> saveUserAndCustomer(User user, Customer customer);
    Mono<User> findByUsername(String username);
}
