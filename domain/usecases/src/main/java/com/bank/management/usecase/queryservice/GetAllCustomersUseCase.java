package com.bank.management.usecase.queryservice;

import com.bank.management.values.Customer;
import com.bank.management.gateway.CustomerRepository;
import reactor.core.publisher.Flux;

public class GetAllCustomersUseCase {

    private final CustomerRepository customerRepository;

    public GetAllCustomersUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Flux<Customer> apply() {
        return customerRepository.findAll();
    }
}
