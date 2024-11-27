package co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_webservice.handlers;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.commands.CreateCustomerCommand;
import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_webservice.din_errors.DinErrors;
import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.din.DinError;
import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.din.DinRequest;
import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.din.DinResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public final class ErrorHandler {
    public static Mono<ServerResponse> handle(
            ServerRequest serverRequest,
            Throwable throwable,
            DinError dinError,
            HttpStatus httpStatus
    ) {
        return serverRequest
                .bodyToMono(new ParameterizedTypeReference<DinRequest<?>>() {})
                .flatMap(dinRequest -> ServerResponse
                        .status(httpStatus)
                        .bodyValue(
                                new DinResponse<>(
                                        dinRequest.getDinHeader(),
                                        DinErrors.withDetail(dinError, throwable.getMessage())
                                )
                        )
                );
    }
}
