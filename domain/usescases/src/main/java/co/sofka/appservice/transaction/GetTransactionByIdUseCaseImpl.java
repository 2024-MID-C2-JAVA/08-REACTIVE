package co.sofka.appservice.transaction;

import co.sofka.Transaction;
import co.sofka.in.transaction.GetTransactionsUseCase;
import co.sofka.out.TransactionRepository;
import reactor.core.publisher.Flux;

public class GetTransactionByIdUseCaseImpl implements GetTransactionsUseCase {

    private final TransactionRepository repository;

    public GetTransactionByIdUseCaseImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<Transaction> apply(Transaction transaction) {
        return repository.getAllTransactions(transaction);
    }
}
