package com.bank.management.usecase.queryservice;

import com.bank.management.customer.Customer;
import com.bank.management.exception.CustomerNotFoundException;
import com.bank.management.gateway.CustomerRepository;
import reactor.core.publisher.Mono;


public class GetCustomerByUsernameUseCase {

    private final CustomerRepository customerRepository;

    public GetCustomerByUsernameUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Mono<Customer> apply(String username) {
        return customerRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found with username: " + username)));
    }
}
