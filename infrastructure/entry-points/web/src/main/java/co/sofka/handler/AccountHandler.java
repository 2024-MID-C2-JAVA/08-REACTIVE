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
    private final GetAccountByIdUseCase getAccountByIdUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;

    public AccountHandler(CreateAccountUseCase createAccountUseCase, GetAccountByIdUseCase getAccountByIdUseCase, DeleteAccountUseCase deleteAccountUseCase) {
        this.createAccountUseCase = createAccountUseCase;
        this.getAccountByIdUseCase = getAccountByIdUseCase;
        this.deleteAccountUseCase = deleteAccountUseCase;
    }

    public Mono<ServerResponse> createAccount(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<Account>>() {
                })
                .flatMap(requestMs -> {

                    Account account = new Account();
                    account.setNumber(requestMs.getDinBody().getNumber());
                    account.setAmount(requestMs.getDinBody().getAmount());
                    account.setCustomerId(requestMs.getDinBody().getCustomerId());

                    return createAccountUseCase.apply(account)
                            .flatMap(createdAccount -> ServerResponse
                                    .status(HttpStatus.CREATED)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(createdAccount))
                            .onErrorResume(error -> ServerResponse
                                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue("Error creating account: " + error.getMessage()));
                });
    }

    public Mono<ServerResponse> deleteAccount(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<AccountDto>>() {})
                .flatMap(requestMs->{
                    Account account = new Account();
                    account.setId(requestMs.getDinBody().getId());
                    return deleteAccountUseCase.apply(account)
                            .flatMap(deletedAccount->ServerResponse
                                    .status(HttpStatus.OK)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(deletedAccount));
                });
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
