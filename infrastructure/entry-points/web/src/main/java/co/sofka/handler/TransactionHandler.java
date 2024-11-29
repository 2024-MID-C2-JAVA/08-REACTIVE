package co.sofka.handler;

import co.sofka.Account;
import co.sofka.RequestMs;
import co.sofka.Transaction;
import co.sofka.appservice.eventsUseCase.CreateTransactionCommandUseCaseImpl;
import co.sofka.commands.CreateTransactionCommand;
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

    private final CreateTransactionCommandUseCaseImpl createTransactionCommandUseCase;

    public TransactionHandler(CreateTransactionCommandUseCaseImpl createTransactionCommandUseCase) {
        this.createTransactionCommandUseCase = createTransactionCommandUseCase;
    }

    public Mono<ServerResponse> createTransaction(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<Transaction>>() {})
                .flatMap(requestMs->{
                    CreateTransactionCommand command=new CreateTransactionCommand();
                    command.setAmount(requestMs.getDinBody().getAmount());
                    command.setType(requestMs.getDinBody().getType());
                    command.setAccountId(requestMs.getDinBody().getAccountId());

                    return createTransactionCommandUseCase.publish(Mono.just(command))
                            .flatMap(transactionCreated->ServerResponse
                                    .status(HttpStatus.OK)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(command));
                });
    }

}
