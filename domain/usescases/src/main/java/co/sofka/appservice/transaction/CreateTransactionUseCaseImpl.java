package co.sofka.appservice.transaction;

import co.sofka.Transaction;
import co.sofka.appservice.transaction.strategy.AccountMovementContext;
import co.sofka.events.TransactionCreatedEvent;
import co.sofka.in.transaction.CreateTransactionUseCase;
import co.sofka.out.TransactionRepository;
import co.sofka.rabbitMq.CreateTransactionEventUseCase;
import reactor.core.publisher.Mono;

public class CreateTransactionUseCaseImpl implements CreateTransactionUseCase {

    private final TransactionRepository repository;
    private final CreateTransactionEventUseCase event;

    public CreateTransactionUseCaseImpl(TransactionRepository repository, CreateTransactionEventUseCase event) {
        this.repository = repository;
        this.event = event;
    }

    @Override
    public Mono<Transaction> apply(Transaction transaction) {
        Transaction transaction1 = AccountMovementContext
                .accountMovement(transaction)
                .movement(transaction);

        return repository.createTransaction(transaction1);
    }

}
