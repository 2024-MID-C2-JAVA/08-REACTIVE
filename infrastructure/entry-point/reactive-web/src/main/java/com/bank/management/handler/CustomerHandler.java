package com.bank.management.handler;

import com.bank.management.command.CreateCustomerCommand;
import com.bank.management.command.DeleteCustomerCommand;
import com.bank.management.ResponseBuilder;
import com.bank.management.data.*;
import com.bank.management.enums.DinErrorCode;
import com.bank.management.exception.CustomerAlreadyExistsException;
import com.bank.management.exception.CustomerNotFoundException;
import com.bank.management.usecase.appservice.CreateCustomerEventUseCase;
import com.bank.management.usecase.appservice.DeleteCustomerEventUseCase;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class CustomerHandler {

    private final CreateCustomerEventUseCase createCustomerEventUseCase;
    private final DeleteCustomerEventUseCase deleteCustomerEventUseCase;

    public CustomerHandler(CreateCustomerEventUseCase createCustomerEventUseCase, DeleteCustomerEventUseCase deleteCustomerEventUseCase) {
        this.createCustomerEventUseCase = createCustomerEventUseCase;
        this.deleteCustomerEventUseCase = deleteCustomerEventUseCase;
    }


    public Mono<ServerResponse> createCustomer(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<CreateCustomerCommand>>() {})
                .flatMap(req -> {
                    DinHeader dinHeader = req.getDinHeader();
                    CreateCustomerCommand command = req.getDinBody();

                    return createCustomerEventUseCase.apply(Mono.just(command))
                            .collectList()
                            .flatMap(customerCreated -> {
                                Map<String, String> responseData = new HashMap<>();
                                return ServerResponse.status(HttpStatus.CREATED)
                                        .bodyValue(Objects.requireNonNull(ResponseBuilder.buildResponse(
                                                dinHeader,
                                                responseData,
                                                DinErrorCode.SUCCESS,
                                                HttpStatus.CREATED,
                                                "Customer creation process completed."
                                        ).getBody()));
                            });
                })
                .onErrorResume(CustomerAlreadyExistsException.class, e -> HandleError.handle(request, DinErrorCode.OPERATION_FAILED, HttpStatus.CONFLICT, e.getMessage()))
                .onErrorResume(IllegalArgumentException.class, e -> HandleError.handle(request, DinErrorCode.BAD_REQUEST, HttpStatus.BAD_REQUEST, e.getMessage()))
                .onErrorResume(e -> HandleError.handle(request, DinErrorCode.OPERATION_FAILED, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    public Mono<ServerResponse> deleteCustomer(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<DeleteCustomerCommand>>() {})
                .flatMap(req -> {
                    DeleteCustomerCommand command = req.getDinBody();

                    return deleteCustomerEventUseCase.apply(command)
                            .flatMap(isDeleted -> {
                                Map<String, String> responseData = new HashMap<>();
                                responseData.put("id", String.valueOf(command.getAggregateRootId()));
                                responseData.put("status", "Processing");

                                return ServerResponse.status(HttpStatus.OK)
                                        .bodyValue(Objects.requireNonNull(
                                                ResponseBuilder.buildResponse(
                                                        req.getDinHeader(),
                                                        responseData,
                                                        DinErrorCode.CUSTOMER_DELETED,
                                                        HttpStatus.OK,
                                                        "Customer deletion process completed."
                                                ).getBody()
                                        ));
                            });
                })
                .onErrorResume(CustomerNotFoundException.class, e -> HandleError.handle(request, DinErrorCode.CUSTOMER_NOT_FOUND, HttpStatus.NOT_FOUND, e.getMessage()))
                .onErrorResume(IllegalArgumentException.class, e -> HandleError.handle(request, DinErrorCode.BAD_REQUEST, HttpStatus.BAD_REQUEST, e.getMessage()))
                .onErrorResume(e -> HandleError.handle(request, DinErrorCode.OPERATION_FAILED, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }


}
