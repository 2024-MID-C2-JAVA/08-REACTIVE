package co.sofka.appservice.customer;

import co.sofka.Customer;
import co.sofka.out.CustomerViewsRepository;
import reactor.core.publisher.Mono;

public class GetCustomerByIdUseCaseView implements co.sofka.in.customer.GetCustomerByIdUseCase {

    private final CustomerViewsRepository repository;

    public GetCustomerByIdUseCaseView(CustomerViewsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Customer> apply(Customer customer) {
        return repository.getCustomer(customer);
    }

}
