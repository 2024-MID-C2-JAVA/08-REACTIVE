package co.sofka.usecase.appEventBank;


import co.sofka.Event;
import co.sofka.commands.request.BankTransactionDepositCajero;
import co.sofka.generic.DomainEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ISaveEventTransactionDepositCajeroService {
    Flux<DomainEvent> apply(Mono<BankTransactionDepositCajero> item);


}