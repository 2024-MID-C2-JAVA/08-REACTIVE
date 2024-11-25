package co.sofka.in.customer;

import co.sofka.Customer;
import reactor.core.publisher.Mono;

public interface GetCustomerByEmailUseCase {
    Mono<Customer> getCustomerByEmail(Customer customer);
}
