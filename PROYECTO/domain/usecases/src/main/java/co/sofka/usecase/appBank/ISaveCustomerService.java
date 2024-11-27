package co.sofka.usecase.appBank;


import co.sofka.Customer;
import co.sofka.Event;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ISaveCustomerService {
    Flux<Customer> apply(Customer item);


}
