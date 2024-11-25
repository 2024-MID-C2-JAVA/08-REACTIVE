package com.bank.management.handler;

import com.bank.management.ResponseBuilder;
import com.bank.management.data.*;
import com.bank.management.enums.DinErrorCode;
import com.bank.management.exception.BankAccountNotFoundException;
import com.bank.management.usecase.queryservice.GetAccountsByCustomerUseCase;
import com.bank.management.usecase.queryservice.GetBankAccountUseCase;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class AccountHandler {

    private final GetAccountsByCustomerUseCase getAccountsByCustomerUseCase;
    private final GetBankAccountUseCase getBankAccountUseCase;

    public AccountHandler(GetAccountsByCustomerUseCase getAccountsByCustomerUseCase,
                          GetBankAccountUseCase getBankAccountUseCase) {
        this.getAccountsByCustomerUseCase = getAccountsByCustomerUseCase;
        this.getBankAccountUseCase = getBankAccountUseCase;
    }

    public Mono<ServerResponse> getBankAccountByCustomer(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<RequestGetBankAccountDTO>>() {})
                .flatMap(req -> getAccountsByCustomerUseCase.apply(req.getDinBody().getId())
                        .map(account -> new ResponseAllBankAccountByCustomerDTO.Builder()
                                .number(account.getNumber())
                                .amount(account.getAmount())
                                .id(account.getId())
                                .build())
                        .collectList()
                        .flatMap(accounts -> ServerResponse.ok()
                                .bodyValue(Objects.requireNonNull(ResponseBuilder.buildResponse(
                                        req.getDinHeader(),
                                        accounts,
                                        DinErrorCode.SUCCESS,
                                        HttpStatus.OK,
                                        "Accounts retrieved successfully.").getBody()))
                        ))
                .onErrorResume(e -> HandleError.handle(request, DinErrorCode.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    public Mono<ServerResponse> getBankAccount(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<RequestGetBankAccountDTO>>() {})
                .flatMap(req -> getBankAccountUseCase.apply(req.getDinBody().getId())
                        .flatMap(account -> {
                            BankAccountDTO accountDTO = new BankAccountDTO.Builder()
                                    .number(account.getNumber())
                                    .amount(account.getAmount())
                                    .build();
                            return ServerResponse.ok()
                                    .bodyValue(Objects.requireNonNull(ResponseBuilder.buildResponse(
                                            req.getDinHeader(),
                                            accountDTO,
                                            DinErrorCode.SUCCESS,
                                            HttpStatus.OK,
                                            "Account information retrieved successfully.").getBody()));
                        }))
                .onErrorResume(BankAccountNotFoundException.class, e -> HandleError.handle(request, DinErrorCode.ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND, e.getMessage()))
                .onErrorResume(e -> HandleError.handle(request, DinErrorCode.UNKNOWN_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }
}

