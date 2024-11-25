package co.sofka.handler;

import co.sofka.Account;
import co.sofka.RequestMs;
import co.sofka.Transaction;
import co.sofka.in.account.GetAccountByIdUseCase;
import co.sofka.in.account.UpdateAccountUseCase;
import co.sofka.in.transaction.CreateTransactionUseCase;
import co.sofka.in.transaction.GetTransactionsUseCase;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class TransactionHandler {

    private final CreateTransactionUseCase transactionUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;
    private final GetAccountByIdUseCase getAccountByIdUseCase;


    public TransactionHandler(CreateTransactionUseCase transactionUseCase, UpdateAccountUseCase updateAccountUseCase, GetAccountByIdUseCase getAccountByIdUseCase) {
        this.transactionUseCase = transactionUseCase;
        this.updateAccountUseCase = updateAccountUseCase;
        this.getAccountByIdUseCase = getAccountByIdUseCase;
    }

    public Mono<ServerResponse> createTransaction(Transaction transaction) {
        return getAccountByIdUseCase.apply(new Account(transaction.getAccountId()))
                .flatMap(accountCreated->{
                    return transactionUseCase.apply(transaction)
                            .flatMap(createTransaction->{
                                accountCreated.setId(transaction.getAccountId());
                                return updateAccountUseCase.apply(accountCreated,transaction)
                                        .flatMap(update->ServerResponse
                                                .status(HttpStatus.OK)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(update));
                            });
                });
    }

}
