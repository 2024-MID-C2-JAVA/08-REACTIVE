package co.sofka.handler;

import co.sofka.Account;
import co.sofka.RequestMs;
import co.sofka.appservice.eventsUseCase.CreateAccountCommandUseCaseImpl;
import co.sofka.commands.CreateAccountCommand;
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

    private final CreateAccountCommandUseCaseImpl createAccountCommandUseCase;

    public AccountHandler(CreateAccountCommandUseCaseImpl createAccountCommandUseCase) {
        this.createAccountCommandUseCase = createAccountCommandUseCase;
    }

    public Mono<ServerResponse> createAccount(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<AccountDto>>() {})
                .flatMap(requestMs->{
                    CreateAccountCommand command= new CreateAccountCommand();
                    command.setCustomerId(requestMs.getDinBody().getCustomerId());
                    command.setAmount(requestMs.getDinBody().getAmount());
                    command.setNumber(Integer.parseInt(requestMs.getDinBody().getNumber()));

                    return createAccountCommandUseCase.publish(Mono.just(command))
                            .flatMap(accountCreated->ServerResponse
                                    .status(HttpStatus.CREATED)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(accountCreated));
                });
    }
}
