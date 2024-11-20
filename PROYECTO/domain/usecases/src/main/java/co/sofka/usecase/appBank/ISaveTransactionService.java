package co.sofka.usecase.appBank;




import co.sofka.Transaction;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ISaveTransactionService {
    Mono<Transaction> save(Transaction transaction);


}
