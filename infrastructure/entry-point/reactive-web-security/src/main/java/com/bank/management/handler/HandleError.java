package com.bank.management.handler;

import com.bank.management.ResponseBuilder;
import com.bank.management.data.DinHeader;
import com.bank.management.data.RequestMs;
import com.bank.management.enums.DinErrorCode;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class HandleError {

    protected static Mono<ServerResponse> handle(ServerRequest request, DinErrorCode errorCode, HttpStatus status, String message) {
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<?>>() {})
                .flatMap(req -> {
                    DinHeader dinHeader = req.getDinHeader();
                    return ServerResponse.status(status)
                            .bodyValue(Objects.requireNonNull(ResponseBuilder.buildResponse(dinHeader, null, errorCode, status, message).getBody()));
                })
                .switchIfEmpty(Mono.defer(() -> ServerResponse.status(status)
                        .bodyValue(Objects.requireNonNull(ResponseBuilder.buildResponse(null, null, errorCode, status, message).getBody()))));
    }
}
