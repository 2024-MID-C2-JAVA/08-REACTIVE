package co.sofka.in.transaction;

import co.sofka.Transaction;
import reactor.core.publisher.Mono;

public interface CreateTransactionUseCase {
    Mono<Transaction>apply(Transaction transaction);
}
