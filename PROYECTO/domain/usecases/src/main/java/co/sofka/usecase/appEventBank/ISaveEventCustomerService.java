package co.sofka.usecase.appEventBank;


import co.sofka.commands.request.CustomerCommand;
import co.sofka.generic.DomainEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ISaveEventCustomerService {
    Flux<DomainEvent> apply(Mono<CustomerCommand> item);


}
