package co.sofka.usecase.appEventBank;


import co.sofka.Customer;
import co.sofka.Event;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ISaveEventCustomerService {
    Flux<Event> apply(Mono<Customer> item);


}
