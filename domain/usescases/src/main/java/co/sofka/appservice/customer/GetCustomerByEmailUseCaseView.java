package co.sofka.appservice.customer;

import co.sofka.Customer;
import co.sofka.out.CustomerViewsRepository;
import reactor.core.publisher.Mono;

public class GetCustomerByEmailUseCaseView implements co.sofka.in.customer.GetCustomerByEmailUseCase {

    private final CustomerViewsRepository repository;

    public GetCustomerByEmailUseCaseView(CustomerViewsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Customer> getCustomerByEmail(Customer customer) {
        return repository.getCustomerByEmail(customer);
    }
}
