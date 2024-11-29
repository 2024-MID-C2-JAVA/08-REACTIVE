package co.sofka.usecase.appEventBank;


import co.sofka.Event;
import co.sofka.commands.request.BankTransactionWithdrawFromATM;
import co.sofka.generic.DomainEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ISaveEventTransactionRetiroCajeroService {
    Flux<DomainEvent> apply(Mono<BankTransactionWithdrawFromATM> item);


}
