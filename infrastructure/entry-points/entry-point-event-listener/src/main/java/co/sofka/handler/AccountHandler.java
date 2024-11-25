package co.sofka.handler;

import co.sofka.Account;
import co.sofka.RequestMs;
import co.sofka.data.account.AccountDto;
import co.sofka.in.account.CreateAccountUseCase;
import co.sofka.in.account.DeleteAccountUseCase;
import co.sofka.in.account.GetAccountByIdUseCase;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class AccountHandler {

    private final CreateAccountUseCase createAccountUseCase;

    public AccountHandler(CreateAccountUseCase createAccountUseCase) {
        this.createAccountUseCase = createAccountUseCase;
    }

    public Mono<ServerResponse> createAccount(Account account) {
        return createAccountUseCase.apply(account)
                .flatMap(accountCreated ->  ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(accountCreated))
                .onErrorResume(error -> ServerResponse
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue("Error creating account: " + error.getMessage()));
    }

}
