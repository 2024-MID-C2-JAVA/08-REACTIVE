package co.sofka.out;

import co.sofka.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionViewsRepository {
    Flux<Transaction>getAllTransactions(Transaction transaction);
}
