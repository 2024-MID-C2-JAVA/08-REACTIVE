package co.sofka.out;

import co.sofka.Customer;
import reactor.core.publisher.Mono;

public interface CustomerViewsRepository {
    Mono<Customer> getCustomer(Customer customer);
    Mono<Customer> getCustomerByEmail(Customer customer);
}
