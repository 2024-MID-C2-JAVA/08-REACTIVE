package co.sofka.appservice.transaction;

import co.sofka.Transaction;
import co.sofka.appservice.transaction.strategy.AccountMovementContext;
import co.sofka.in.transaction.CreateTransactionUseCase;
import co.sofka.out.TransactionRepository;
import reactor.core.publisher.Mono;

public class CreateTransactionUseCaseImpl implements CreateTransactionUseCase {

    private final TransactionRepository repository;

    public CreateTransactionUseCaseImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Transaction> apply(Transaction transaction) {
        Transaction transaction1 = AccountMovementContext
                .accountMovement(transaction)
                .movement(transaction);

        return repository.createTransaction(transaction1)
                .then(Mono.just(transaction1));
    }

}
