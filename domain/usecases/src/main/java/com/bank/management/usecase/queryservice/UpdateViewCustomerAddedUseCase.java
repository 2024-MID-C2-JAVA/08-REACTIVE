package com.bank.management.usecase.queryservice;

import com.bank.management.values.Customer;
import com.bank.management.exception.CustomerAlreadyExistsException;
import com.bank.management.exception.CustomerNotFoundException;
import com.bank.management.gateway.CustomerRepository;
import reactor.core.publisher.Mono;

public class UpdateViewCustomerAddedUseCase {

    private final CustomerRepository customerRepository;

    public UpdateViewCustomerAddedUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Mono<Customer> apply(Customer customer) {
        return customerRepository.findByUsername(customer.getUsername().value())
                .flatMap(existingCustomer ->
                        Mono.<Customer>error(new CustomerAlreadyExistsException(customer.getUsername().value())))
                .onErrorResume(CustomerNotFoundException.class, e ->
                        customerRepository.save(customer));
    }

}
