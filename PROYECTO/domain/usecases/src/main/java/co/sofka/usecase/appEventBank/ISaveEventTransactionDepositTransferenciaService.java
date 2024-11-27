package co.sofka.usecase.appEventBank;


import co.sofka.Event;
import co.sofka.dto.BankTransactionDepositSucursal;
import co.sofka.dto.BankTransactionDepositTransfer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ISaveEventTransactionDepositTransferenciaService {
    Flux<Event> apply(Mono<BankTransactionDepositTransfer> item);


}
