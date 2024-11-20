package co.sofka.gateway;


import co.sofka.Customer;
import co.sofka.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITransactionRepository {

    Mono<Transaction> save(Transaction id);

    Flux<Transaction> getAll();

}
