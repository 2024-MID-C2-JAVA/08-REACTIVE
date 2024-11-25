package com.bank.management.usecase.queryservice;

import com.bank.management.customer.Customer;
import com.bank.management.exception.CustomerNotFoundException;
import com.bank.management.gateway.CustomerRepository;
import reactor.core.publisher.Mono;


public class GetCustomerByIdUseCase {

    private final CustomerRepository customerRepository;

    public GetCustomerByIdUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Mono<Customer> apply(String id) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException(id)));
    }
}
