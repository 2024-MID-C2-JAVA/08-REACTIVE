package com.bank.management.handler;

import com.bank.management.ResponseBuilder;
import com.bank.management.data.*;
import com.bank.management.enums.DinErrorCode;
import com.bank.management.exception.CustomerNotFoundException;
import com.bank.management.usecase.queryservice.GetAllCustomersUseCase;
import com.bank.management.usecase.queryservice.GetCustomerByIdUseCase;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class CustomerHandler {

    private final GetAllCustomersUseCase getAllCustomersUseCase;
    private final GetCustomerByIdUseCase getCustomerByIdUseCase;

    public CustomerHandler(GetAllCustomersUseCase getAllCustomersUseCase,
                           GetCustomerByIdUseCase getCustomerByIdUseCase) {
        this.getAllCustomersUseCase = getAllCustomersUseCase;
        this.getCustomerByIdUseCase = getCustomerByIdUseCase;
    }

    public Mono<ServerResponse> getAllCustomers(ServerRequest request) {
        return getAllCustomersUseCase.apply()
                .map(customer -> new AllCustomerDTO.Builder()
                        .setUsername(customer.getUsername())
                        .setLastname(customer.getLastname())
                        .setName(customer.getName())
                        .setId(customer.getId())
                        .build())
                .collectList()
                .flatMap(customers -> ServerResponse.ok()
                        .bodyValue(Objects.requireNonNull(ResponseBuilder.buildResponse(null, customers, DinErrorCode.SUCCESS, HttpStatus.OK, "All customers retrieved successfully.").getBody())))
                .onErrorResume(e -> HandleError.handle(request, DinErrorCode.OPERATION_FAILED, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    public Mono<ServerResponse> getCustomerById(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<RequestGetCustomerDTO>>() {})
                .flatMap(req -> getCustomerByIdUseCase.apply(req.getDinBody().getId())
                        .flatMap(customer -> {
                            CustomerDTO customerDTO = new CustomerDTO.Builder()
                                    .setUsername(customer.getUsername())
                                    .setLastname(customer.getLastname())
                                    .setName(customer.getName())
                                    .build();

                            return ServerResponse.ok()
                                    .bodyValue(Objects.requireNonNull(ResponseBuilder.buildResponse(
                                            req.getDinHeader(),
                                            customerDTO,
                                            DinErrorCode.SUCCESS,
                                            HttpStatus.OK,
                                            "Customer retrieved successfully."
                                    ).getBody()));
                        }))
                .onErrorResume(CustomerNotFoundException.class, e -> HandleError.handle(request, DinErrorCode.CUSTOMER_NOT_FOUND, HttpStatus.NOT_FOUND, e.getMessage()))
                .onErrorResume(IllegalArgumentException.class, e -> HandleError.handle(request, DinErrorCode.BAD_REQUEST, HttpStatus.BAD_REQUEST, e.getMessage()))
                .onErrorResume(e -> HandleError.handle(request, DinErrorCode.OPERATION_FAILED, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

}
