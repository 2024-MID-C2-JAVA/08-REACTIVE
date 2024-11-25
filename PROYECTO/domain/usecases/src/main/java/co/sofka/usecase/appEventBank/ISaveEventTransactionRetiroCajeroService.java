package co.sofka.usecase.appEventBank;


import co.sofka.Event;
import co.sofka.dto.BankTransactionDepositCajero;
import co.sofka.dto.BankTransactionWithdrawFromATM;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ISaveEventTransactionRetiroCajeroService {
    Flux<Event> apply(Mono<BankTransactionWithdrawFromATM> item);


}
