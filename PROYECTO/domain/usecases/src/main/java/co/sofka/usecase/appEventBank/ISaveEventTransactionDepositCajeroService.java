package co.sofka.usecase.appEventBank;


import co.sofka.Event;
import co.sofka.dto.BankTransactionDepositCajero;
import co.sofka.dto.BankTransactionDepositSucursal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ISaveEventTransactionDepositCajeroService {
    Flux<Event> apply(Mono<BankTransactionDepositCajero> item);


}
