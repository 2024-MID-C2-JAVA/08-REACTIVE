package com.bank.management.handler;

import com.bank.management.ResponseBuilder;
import com.bank.management.command.DeleteBankAccountCommand;
import com.bank.management.data.*;
import com.bank.management.enums.DinErrorCode;
import com.bank.management.exception.AccountCreationException;
import com.bank.management.exception.BankAccountNotFoundException;
import com.bank.management.exception.CustomerNotFoundException;
import com.bank.management.usecase.appservice.CreateBankAccountEventUseCase;
import com.bank.management.usecase.appservice.DeleteBankAccountEventUseCase;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import com.bank.management.command.CreateAccountCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class AccountHandler {

    private final CreateBankAccountEventUseCase createBankAccountEventUseCase;
    private final DeleteBankAccountEventUseCase deleteBankAccountEventUseCase;

    public AccountHandler(CreateBankAccountEventUseCase createBankAccountEventUseCase, DeleteBankAccountEventUseCase deleteBankAccountEventUseCase) {
        this.createBankAccountEventUseCase = createBankAccountEventUseCase;
        this.deleteBankAccountEventUseCase = deleteBankAccountEventUseCase;
    }

    public Mono<ServerResponse> createAccount(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<CreateAccountCommand>>() {})
                .flatMap(req -> {
                    CreateAccountCommand command = req.getDinBody();

                    return createBankAccountEventUseCase.apply(Mono.just(command))
                            .collectList()
                            .flatMap(accountCreated -> {
                                Map<String, String> responseData = new HashMap<>();
                                return ServerResponse.status(HttpStatus.CREATED)
                                        .bodyValue(Objects.requireNonNull(ResponseBuilder.buildResponse(
                                                req.getDinHeader(),
                                                responseData,
                                                DinErrorCode.SUCCESS,
                                                HttpStatus.CREATED,
                                                "Account creation process completed.").getBody()));
                            });
                })
                .onErrorResume(CustomerNotFoundException.class, e -> HandleError.handle(request, DinErrorCode.ERROR_CREATING_ACCOUNT, HttpStatus.NOT_FOUND, e.getMessage()))
                .onErrorResume(AccountCreationException.class, e -> HandleError.handle(request, DinErrorCode.ERROR_CREATING_ACCOUNT, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()))
                .onErrorResume(e -> HandleError.handle(request, DinErrorCode.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    public Mono<ServerResponse> deleteBankAccount(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<DeleteBankAccountCommand>>() {})
                .flatMap(req -> {
                    DeleteBankAccountCommand command = req.getDinBody();

                    return deleteBankAccountEventUseCase.apply(command)
                            .flatMap(isDeleted -> {
                                Map<String, String> responseData = new HashMap<>();
                                responseData.put("id", command.getAggregateRootId());
                                responseData.put("status","Processing");

                                return ServerResponse.status(HttpStatus.OK)
                                        .bodyValue(Objects.requireNonNull(
                                                ResponseBuilder.buildResponse(
                                                        req.getDinHeader(),
                                                        responseData,
                                                        DinErrorCode.SUCCESS,
                                                        HttpStatus.OK,
                                                        "Bank account deletion process completed."
                                                ).getBody()
                                        ));
                            });
                })
                .onErrorResume(BankAccountNotFoundException.class, e ->
                        HandleError.handle(request, DinErrorCode.ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND, e.getMessage()))
                .onErrorResume(e ->
                        HandleError.handle(request, DinErrorCode.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }
}

