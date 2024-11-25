package com.bank.management.handler;

import com.bank.management.command.ProcessDepositCommand;
import com.bank.management.command.ProcessPurchaseCommand;
import com.bank.management.command.ProcessWithdrawalCommand;
import com.bank.management.generic.Command;
import com.bank.management.transaction.Purchase;
import com.bank.management.ResponseBuilder;
import com.bank.management.transaction.Withdrawal;
import com.bank.management.data.*;
import com.bank.management.enums.DinErrorCode;
import com.bank.management.usecase.appservice.EncryptionUseCase;
import com.bank.management.usecase.appservice.ProcessDepositEventUseCase;
import com.bank.management.usecase.appservice.ProcessPurchaseEventUseCase;
import com.bank.management.usecase.appservice.ProcessWithdrawEventUseCase;
import com.bank.management.usecase.queryservice.ProcessPurchaseWithCardUseCase;
import com.bank.management.usecase.queryservice.ProcessWithdrawUseCase;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Component
public class TransactionsHandler {

    private final EncryptionUseCase encryptionUseCase;
    private final ProcessDepositEventUseCase processDepositEventUseCase;
    private final ProcessPurchaseEventUseCase processPurchaseEventUseCase;
    private final ProcessWithdrawEventUseCase processWithdrawEventUseCase;

    public TransactionsHandler(EncryptionUseCase encryptionUseCase, ProcessDepositEventUseCase processDepositEventUseCase, ProcessPurchaseEventUseCase processPurchaseEventUseCase, ProcessWithdrawEventUseCase processWithdrawEventUseCase) {
        this.encryptionUseCase = encryptionUseCase;
        this.processDepositEventUseCase = processDepositEventUseCase;
        this.processPurchaseEventUseCase = processPurchaseEventUseCase;
        this.processWithdrawEventUseCase = processWithdrawEventUseCase;
    }

    public Mono<ServerResponse> processDeposit(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<ProcessDepositCommand>>() {})
                .zipWith(request.principal())
                .flatMap(tuple -> {
                    RequestMs<ProcessDepositCommand> req = tuple.getT1();
                    Principal principal = tuple.getT2();

                    ProcessDepositCommand command = req.getDinBody();

                    String authenticatedUsername = principal.getName();
                    if (!authenticatedUsername.equals(req.getDinBody().getUsername())) {
                        return HandleError.handle(request, DinErrorCode.ACCOUNT_DOESNT_BELONG, HttpStatus.FORBIDDEN, "Account does not belong to authenticated user.");
                    }

                    String encryptedAccountNumber = req.getDinBody().getAccountNumber();
                    String symmetricKey = req.getDinHeader().getSymmetricKey();
                    String initializationVector = req.getDinHeader().getInitializationVector();

                    String decryptedAccountNumber = encryptionUseCase.decryptData(encryptedAccountNumber, symmetricKey, initializationVector);
                    command.setAccountNumber(decryptedAccountNumber);
                    return processDepositEventUseCase.apply(command)
                            .flatMap(account -> {
                                Map<String, String> responseData = new HashMap<>();
                                responseData.put("accountNumber", encryptedAccountNumber);

                                return ServerResponse.ok().bodyValue(ResponseBuilder.buildResponse(
                                        req.getDinHeader(), responseData, DinErrorCode.SUCCESS, HttpStatus.OK, "Deposit was successful."));
                            })
                            .onErrorResume(e -> HandleError.handle(request, DinErrorCode.DEPOSIT_FAILED, HttpStatus.BAD_REQUEST, e.getMessage()));
                });
    }

    public Mono<ServerResponse> processPurchase(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<ProcessPurchaseCommand>>() {})
                .flatMap(req -> {
                    String encryptedAccountNumber = req.getDinBody().getAccountNumber();
                    String symmetricKey = req.getDinHeader().getSymmetricKey();
                    String initializationVector = req.getDinHeader().getInitializationVector();

                    String decryptedAccountNumber = encryptionUseCase.decryptData(encryptedAccountNumber, symmetricKey, initializationVector);

                    ProcessPurchaseCommand command = req.getDinBody();
                    command.setAccountNumber(decryptedAccountNumber);

                    return processPurchaseEventUseCase.apply(command)
                            .flatMap(account -> {
                                Map<String, String> responseData = new HashMap<>();
                                responseData.put("accountNumber", encryptedAccountNumber);

                                return ServerResponse.ok().bodyValue(ResponseBuilder.buildResponse(
                                        req.getDinHeader(), responseData, DinErrorCode.SUCCESS, HttpStatus.OK, "Purchase was successful."));
                            })
                            .onErrorResume(e -> HandleError.handle(request, DinErrorCode.PURCHASE_FAILED, HttpStatus.BAD_REQUEST, e.getMessage()));
                });
    }

    public Mono<ServerResponse> processWithdraw(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<ProcessWithdrawalCommand>>() {})
                .zipWith(request.principal())
                .flatMap(tuple -> {
                    RequestMs<ProcessWithdrawalCommand> req = tuple.getT1();
                    Principal principal = tuple.getT2();

                    String authenticatedUsername = principal.getName();
                    if (!authenticatedUsername.equals(req.getDinBody().getUsername())) {
                        return HandleError.handle(request, DinErrorCode.ACCOUNT_DOESNT_BELONG, HttpStatus.FORBIDDEN, "Account does not belong to authenticated user.");
                    }

                    String encryptedAccountNumber = req.getDinBody().getAccountNumber();
                    String symmetricKey = req.getDinHeader().getSymmetricKey();
                    String initializationVector = req.getDinHeader().getInitializationVector();

                    String decryptedAccountNumber = encryptionUseCase.decryptData(encryptedAccountNumber, symmetricKey, initializationVector);
                    ProcessWithdrawalCommand command = req.getDinBody();
                    command.setAccountNumber(decryptedAccountNumber);

                    return processWithdrawEventUseCase.apply(command)
                            .flatMap(account -> {
                                Map<String, String> responseData = new HashMap<>();
                                responseData.put("accountNumber", encryptedAccountNumber);

                                return ServerResponse.ok().bodyValue(ResponseBuilder.buildResponse(
                                        req.getDinHeader(), responseData, DinErrorCode.SUCCESS, HttpStatus.OK, "Withdrawal was successful."));
                            })
                            .onErrorResume(e -> HandleError.handle(request, DinErrorCode.WITHDRAWAL_FAILED, HttpStatus.BAD_REQUEST, e.getMessage()));
                });
    }
}
