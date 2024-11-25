package com.bank.management.handler;

import com.bank.management.ResponseBuilder;
import com.bank.management.data.*;
import com.bank.management.enums.DinErrorCode;
import com.bank.management.exception.AccountCreationException;
import com.bank.management.exception.BankAccountNotFoundException;
import com.bank.management.exception.CustomerNotFoundException;
import com.bank.management.usecase.appservice.CreateBankAccountEventUseCase;
import com.bank.management.usecase.appservice.DeleteBankAccountUseCase;
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

    private final CreateBankAccountEventUseCase createBankAccountUseCase;
    private final DeleteBankAccountUseCase deleteBankAccountUseCase;

    public AccountHandler(CreateBankAccountEventUseCase createBankAccountUseCase,
                          DeleteBankAccountUseCase deleteBankAccountUseCase) {
        this.createBankAccountUseCase = createBankAccountUseCase;
        this.deleteBankAccountUseCase = deleteBankAccountUseCase;
    }

    public Mono<ServerResponse> createAccount(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<CreateAccountCommand>>() {})
                .flatMap(req -> {
                    CreateAccountCommand command = req.getDinBody();

                    return createBankAccountUseCase.apply(command)
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
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<RequestGetBankAccountDTO>>() {})
                .flatMap(req -> deleteBankAccountUseCase.apply(req.getDinBody().getId())
                        .flatMap(isDeleted -> {
                            Map<String, String> responseData = new HashMap<>();
                            responseData.put("accountNumber", req.getDinBody().getId());
                            return ServerResponse.status(isDeleted ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR)
                                    .bodyValue(Objects.requireNonNull(ResponseBuilder.buildResponse(
                                            req.getDinHeader(),
                                            responseData,
                                            isDeleted ? DinErrorCode.SUCCESS : DinErrorCode.ERROR_DELETING_ACCOUNT,
                                            isDeleted ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR,
                                            isDeleted ? "Bank account deleted successfully." : "Failed to delete bank account.").getBody()));
                        }))
                .onErrorResume(BankAccountNotFoundException.class, e -> HandleError.handle(request, DinErrorCode.ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND, e.getMessage()))
                .onErrorResume(e -> HandleError.handle(request, DinErrorCode.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }
}

