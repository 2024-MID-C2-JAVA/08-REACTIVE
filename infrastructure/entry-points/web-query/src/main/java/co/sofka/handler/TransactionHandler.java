package co.sofka.handler;

import co.sofka.RequestMs;
import co.sofka.Transaction;
import co.sofka.appservice.transaction.GetTransactionByIdUseCaseView;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class TransactionHandler {

    private final GetTransactionByIdUseCaseView getTransactionsUseCase;

    public TransactionHandler(GetTransactionByIdUseCaseView getTransactionsUseCase) {
        this.getTransactionsUseCase = getTransactionsUseCase;
    }

    public Mono<ServerResponse> getTransactionsByUserId(ServerRequest request) {
        return request.bodyToFlux(new ParameterizedTypeReference<RequestMs<Transaction>>() {})
                .flatMap(requestMs -> {
                    Transaction transaction = new Transaction();
                    transaction.setId(requestMs.getDinBody().getId());
                    return getTransactionsUseCase.apply(transaction);
                }).collectList()
                .flatMap(transactions -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(transactions));
    }
    
}
