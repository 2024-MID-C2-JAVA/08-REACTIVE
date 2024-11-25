package co.sofka.appservice.transaction;

import co.sofka.Transaction;
import co.sofka.in.transaction.GetTransactionsUseCase;
import co.sofka.out.TransactionViewsRepository;
import reactor.core.publisher.Flux;

public class GetTransactionByIdUseCaseView implements GetTransactionsUseCase {

    private final TransactionViewsRepository repository;

    public GetTransactionByIdUseCaseView(TransactionViewsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<Transaction> apply(Transaction transaction) {
        return repository.getAllTransactions(transaction);
    }
}
