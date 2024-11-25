package co.sofka.in.transaction;

import co.sofka.Transaction;
import reactor.core.publisher.Flux;

public interface GetTransactionsUseCase {
    Flux<Transaction>apply(Transaction transaction);
}
