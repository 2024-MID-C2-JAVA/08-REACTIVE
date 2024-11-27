package co.sofka.gateway;


import co.sofka.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICustomerRepository {

    Mono<Customer> save(Customer customer);
    Mono<Customer> findById(String id);
    Mono<Customer> findByUsername(String username);
    Flux<Customer> getAll();
}
