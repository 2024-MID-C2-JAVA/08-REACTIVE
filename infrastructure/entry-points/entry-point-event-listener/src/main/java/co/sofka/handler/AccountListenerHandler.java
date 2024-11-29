package co.sofka.handler;

import co.sofka.Account;
import co.sofka.appservice.account.CreateAccountUseCaseImpl;
import co.sofka.in.account.CreateAccountUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class AccountListenerHandler {

    private final CreateAccountUseCaseImpl createAccountUseCase;

    public AccountListenerHandler(CreateAccountUseCaseImpl createAccountUseCase) {
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
