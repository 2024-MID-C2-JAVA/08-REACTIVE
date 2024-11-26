package com.bank.management.usecase.queryservice;

import com.bank.management.customer.Customer;
import com.bank.management.exception.CustomerAlreadyExistsException;
import com.bank.management.exception.CustomerNotFoundException;
import com.bank.management.gateway.CustomerRepository;
import reactor.core.publisher.Mono;

public class CreateCustomerUseCase {

    private final CustomerRepository customerRepository;

    public CreateCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Mono<Customer> apply(Customer customer) {
        return customerRepository.findByUsername(customer.getUsername())
                .flatMap(existingCustomer ->
                        Mono.<Customer>error(new CustomerAlreadyExistsException(customer.getUsername())))
                .onErrorResume(CustomerNotFoundException.class, e ->
                        customerRepository.save(customer));
    }

}
