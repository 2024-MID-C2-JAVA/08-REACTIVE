package co.sofka.appservice.customer;

import co.sofka.Customer;
import co.sofka.in.customer.DeleteCustomerUseCase;
import co.sofka.out.CustomerRepository;
import reactor.core.publisher.Mono;

public class DeleteCustomerUseCaseImpl implements DeleteCustomerUseCase {

    private final CustomerRepository repository;

    public DeleteCustomerUseCaseImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Customer> apply(Customer customer) {
        return repository.deleteCustomer(customer);
    }

}
