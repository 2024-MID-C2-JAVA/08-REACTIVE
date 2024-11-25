package co.sofka.appservice.customer;

import co.sofka.Customer;
import co.sofka.in.customer.CreateCustomerUseCase;
import co.sofka.out.CustomerRepository;
import reactor.core.publisher.Mono;

public class CreateCustomerUseCaseImpl implements CreateCustomerUseCase {

    private final CustomerRepository repository;

    public CreateCustomerUseCaseImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Customer> apply(Customer customer) {
        return repository.createCustomer(customer);
    }

}
