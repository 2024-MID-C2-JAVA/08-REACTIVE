package co.sofka.handler;

import co.sofka.Account;
import co.sofka.RequestMs;
import co.sofka.appservice.account.GetAccountByIdUseCaseView;
import co.sofka.data.account.AccountDto;
import co.sofka.in.account.GetAccountByIdUseCase;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class AccountViewsHandler {

    private final GetAccountByIdUseCaseView getAccountByIdUseCase;

    public AccountViewsHandler(GetAccountByIdUseCaseView getAccountByIdUseCase) {
        this.getAccountByIdUseCase = getAccountByIdUseCase;
    }

    public Mono<ServerResponse> getAccountById(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<AccountDto>>() {})
                .flatMap(requestMs->{
                    Account account2 = new Account();
                    account2.setId(requestMs.getDinBody().getId());
                    account2.setAmount(requestMs.getDinBody().getAmount());
                    account2.setCustomerId(requestMs.getDinBody().getId());
                    account2.setCreatedAt(requestMs.getDinBody().getCreatedAt());
                    return getAccountByIdUseCase.apply(account2)
                            .flatMap(account -> ServerResponse
                                    .status(HttpStatus.OK)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(account));
                });
    }
}
