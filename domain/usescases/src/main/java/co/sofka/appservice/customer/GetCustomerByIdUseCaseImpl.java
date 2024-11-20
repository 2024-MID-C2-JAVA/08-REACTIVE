package co.sofka.appservice.customer;

import co.sofka.Customer;
import co.sofka.in.customer.GetCustomerByIdUseCase;
import co.sofka.out.CustomerRepository;
import reactor.core.publisher.Mono;

public class GetCustomerByIdUseCaseImpl implements GetCustomerByIdUseCase {

    private final CustomerRepository repository;

    public GetCustomerByIdUseCaseImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Customer> apply(Customer customer) {
        return repository.getCustomer(customer);
    }

}
