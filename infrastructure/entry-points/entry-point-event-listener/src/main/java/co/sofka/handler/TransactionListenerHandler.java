package co.sofka.handler;

import co.sofka.Account;
import co.sofka.Transaction;
import co.sofka.appservice.account.GetAccountByIdUseCaseImpl;
import co.sofka.appservice.account.UpdateAccountUseCaseImpl;
import co.sofka.appservice.transaction.CreateTransactionUseCaseImpl;
import co.sofka.in.account.GetAccountByIdUseCase;
import co.sofka.in.account.UpdateAccountUseCase;
import co.sofka.in.transaction.CreateTransactionUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class TransactionListenerHandler {

    private final CreateTransactionUseCaseImpl transactionUseCase;
    private final UpdateAccountUseCaseImpl updateAccountUseCase;
    private final GetAccountByIdUseCaseImpl getAccountByIdUseCase;


    public TransactionListenerHandler(CreateTransactionUseCaseImpl transactionUseCase, UpdateAccountUseCaseImpl updateAccountUseCase, GetAccountByIdUseCaseImpl getAccountByIdUseCase) {
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
