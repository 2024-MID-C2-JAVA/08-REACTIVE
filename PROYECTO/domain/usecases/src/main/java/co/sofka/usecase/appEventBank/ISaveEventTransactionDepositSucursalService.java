package co.sofka.usecase.appEventBank;


import co.sofka.commands.request.BankTransactionDepositSucursal;
import co.sofka.generic.DomainEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ISaveEventTransactionDepositSucursalService {
    Flux<DomainEvent> apply(Mono<BankTransactionDepositSucursal> item);


}
