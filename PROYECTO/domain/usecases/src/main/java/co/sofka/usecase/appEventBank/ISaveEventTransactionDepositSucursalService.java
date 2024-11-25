package co.sofka.usecase.appEventBank;


import co.sofka.Customer;
import co.sofka.Event;
import co.sofka.dto.BankTransactionDepositSucursal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ISaveEventTransactionDepositSucursalService {
    Flux<Event> apply(Mono<BankTransactionDepositSucursal> item);


}
