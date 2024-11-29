package co.sofka.in.customer;

import co.sofka.Customer;
import reactor.core.publisher.Mono;

public interface DeleteCustomerUseCase {
    Mono<Customer>apply(Customer customer);
}
