package co.sofka.usecase.appEventBank;


import co.sofka.Event;
import co.sofka.dto.BankTransactionBuys;
import co.sofka.dto.BankTransactionWithdrawFromATM;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ISaveEventTransactionCompraService {
    Flux<Event> apply(Mono<BankTransactionBuys> item);


}
