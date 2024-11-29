package co.sofka.usecase.appEventBank;


import co.sofka.Event;
import co.sofka.commands.request.BankTransactionDepositTransfer;
import co.sofka.generic.DomainEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ISaveEventTransactionDepositTransferenciaService {
    Flux<DomainEvent> apply(Mono<BankTransactionDepositTransfer> item);


}
