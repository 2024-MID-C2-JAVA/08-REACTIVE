package co.sofka.out;

import co.sofka.Customer;
import reactor.core.publisher.Mono;

public interface CustomerRepository {
    Mono<Customer> createCustomer(Customer customer);
    Mono<Customer> deleteCustomer(Customer customer);
    Mono<Customer> getCustomer(Customer customer);
    Mono<Customer> getCustomerByEmail(Customer customer);
}
