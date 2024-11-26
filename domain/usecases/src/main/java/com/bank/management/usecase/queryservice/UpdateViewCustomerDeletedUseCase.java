package com.bank.management.usecase.queryservice;

import com.bank.management.exception.CustomerNotFoundException;
import com.bank.management.gateway.CustomerRepository;
import reactor.core.publisher.Mono;

public class UpdateViewCustomerDeletedUseCase {

    private final CustomerRepository customerRepository;

    public UpdateViewCustomerDeletedUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Mono<Boolean> apply(String id) {
        return customerRepository.findById(id)
                .flatMap(customer ->
                        customerRepository.delete(customer)
                                .thenReturn(true))
                .onErrorResume(CustomerNotFoundException.class, Mono::error);
    }
}
