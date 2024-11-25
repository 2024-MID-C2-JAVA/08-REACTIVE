package com.bank.management.handler;

import com.bank.management.customer.Customer;
import com.bank.management.ResponseBuilder;
import com.bank.management.data.*;
import com.bank.management.enums.DinErrorCode;
import com.bank.management.exception.CustomerAlreadyExistsException;
import com.bank.management.exception.CustomerNotFoundException;
import com.bank.management.usecase.appservice.CreateCustomerUseCase;
import com.bank.management.usecase.appservice.DeleteCustomerUseCase;
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

    private final CreateCustomerUseCase createCustomerUseCase;
    private final DeleteCustomerUseCase deleteCustomerUseCase;

    public CustomerHandler(CreateCustomerUseCase createCustomerUseCase,
                           DeleteCustomerUseCase deleteCustomerUseCase) {
        this.createCustomerUseCase = createCustomerUseCase;
        this.deleteCustomerUseCase = deleteCustomerUseCase;
    }

    public Mono<ServerResponse> createCustomer(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<RequestCreateCustomerDTO>>() {})
                .flatMap(req -> {
                    DinHeader dinHeader = req.getDinHeader();

                    Customer customerDomain = new Customer.Builder()
                            .username(req.getDinBody().getUsername())
                            .lastname(req.getDinBody().getLastname())
                            .name(req.getDinBody().getName())
                            .build();

                    return createCustomerUseCase.apply(customerDomain)
                            .flatMap(customerCreated -> {
                                Map<String, String> responseData = new HashMap<>();
                                responseData.put("username", customerCreated.getUsername());
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
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<RequestGetCustomerDTO>>() {})
                .flatMap(req -> deleteCustomerUseCase.apply(req.getDinBody().getId())
                        .flatMap(isDeleted -> {
                            Map<String, String> responseData = new HashMap<>();
                            responseData.put("id", String.valueOf(req.getDinBody().getId()));

                            return ServerResponse.status(isDeleted ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR)
                                    .bodyValue(Objects.requireNonNull(ResponseBuilder.buildResponse(
                                            req.getDinHeader(),
                                            responseData,
                                            isDeleted ? DinErrorCode.CUSTOMER_DELETED : DinErrorCode.OPERATION_FAILED,
                                            isDeleted ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR,
                                            isDeleted ? "Customer deleted successfully." : "Error deleting customer."
                                    ).getBody()));
                        }))
                .onErrorResume(CustomerNotFoundException.class, e -> HandleError.handle(request, DinErrorCode.CUSTOMER_NOT_FOUND, HttpStatus.NOT_FOUND, e.getMessage()))
                .onErrorResume(IllegalArgumentException.class, e -> HandleError.handle(request, DinErrorCode.BAD_REQUEST, HttpStatus.BAD_REQUEST, e.getMessage()))
                .onErrorResume(e -> HandleError.handle(request, DinErrorCode.OPERATION_FAILED, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

}
