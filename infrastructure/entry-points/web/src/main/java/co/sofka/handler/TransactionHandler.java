package co.sofka.handler;

import co.sofka.Account;
import co.sofka.Transaction;
import co.sofka.data.transaction.TransactionDto;
import co.sofka.in.account.GetAccountByIdUseCase;
import co.sofka.in.account.UpdateAccountUseCase;
import co.sofka.in.transaction.CreateTransactionUseCase;
import co.sofka.in.transaction.GetTransactionsUseCase;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TransactionHandler {

    private final CreateTransactionUseCase transactionUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;
    private final GetAccountByIdUseCase getAccountByIdUseCase;
    private final GetTransactionsUseCase getTransactionsUseCase;

    public TransactionHandler(CreateTransactionUseCase transactionUseCase, UpdateAccountUseCase updateAccountUseCase, GetAccountByIdUseCase getAccountByIdUseCase, GetTransactionsUseCase getTransactionsUseCase) {
        this.transactionUseCase = transactionUseCase;
        this.updateAccountUseCase = updateAccountUseCase;
        this.getAccountByIdUseCase = getAccountByIdUseCase;
        this.getTransactionsUseCase = getTransactionsUseCase;
    }

    public Mono<Transaction> createTransaction(TransactionDto transactionDTO) {
        return getAccountByIdUseCase.apply(new Account(transactionDTO.getAccountId()))
                .flatMap(account -> {

                    Transaction transaction = new Transaction();
                    transaction.setAmount(transactionDTO.getAmount());
                    transaction.setType(transactionDTO.getType());
                    transaction.setAccountId(transactionDTO.getAccountId());

                    return transactionUseCase.apply(transaction)
                            .flatMap(createdTransaction -> {
                                account.setId(transactionDTO.getAccountId());

                                return updateAccountUseCase.apply(account, createdTransaction)
                                        .thenReturn(createdTransaction);
                            });
                });
    }


    public Flux<Transaction> getTransactionsByUserId(TransactionDto transactionDto){
        Transaction transaction = new Transaction();
        transaction.setId(transactionDto.getId());
        return getTransactionsUseCase.apply(transaction);
    }
}
