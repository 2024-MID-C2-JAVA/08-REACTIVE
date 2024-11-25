package co.sofka.appservice.customer;

import co.sofka.Customer;
import co.sofka.in.customer.GetCustomerByEmailUseCase;
import co.sofka.out.CustomerRepository;
import reactor.core.publisher.Mono;

public class GetCustomerByEmailUseCaseImpl implements GetCustomerByEmailUseCase {

    private final CustomerRepository repository;

    public GetCustomerByEmailUseCaseImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Customer> getCustomerByEmail(Customer customer) {
        return repository.getCustomerByEmail(customer);
    }
}
