package co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_webservice.handlers;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.TransactionType;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.commands.PerformUnidirectionalTransactionCommand;
import co.com.sofka.cuentaflex.libs.domain.use_cases.command_handlers.PerformUnidirectionalTransactionCommandHandler;
import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_webservice.data.PerformUnidirectionalTransactionRequest;
import co.com.sofka.cuentaflex.libs.infrastructure.utils.din.DinRequest;
import co.com.sofka.cuentaflex.libs.infrastructure.utils.din.DinResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public final class PerformUnidirectionalTransactionHandler {
    private final PerformUnidirectionalTransactionCommandHandler performUnidirectionalTransactionCommandHandler;

    public PerformUnidirectionalTransactionHandler(PerformUnidirectionalTransactionCommandHandler performUnidirectionalTransactionCommandHandler) {
        this.performUnidirectionalTransactionCommandHandler = performUnidirectionalTransactionCommandHandler;
    }

    public Mono<ServerResponse> handle(ServerRequest serverRequest, TransactionType transactionType) {
        return serverRequest.bodyToMono(
                        new ParameterizedTypeReference<DinRequest<PerformUnidirectionalTransactionRequest>>() {
                        }
                ).flatMap(dinRequest ->
                        performUnidirectionalTransactionCommandHandler.apply(
                                new PerformUnidirectionalTransactionCommand(
                                        dinRequest.getDinBody().getCustomerId(),
                                        dinRequest.getDinBody().getAccountId(),
                                        dinRequest.getDinBody().getTransactionId(),
                                        dinRequest.getDinBody().getAmount(),
                                        transactionType
                                )
                        ).then(Mono.just(dinRequest))
                )
                .flatMap(dinRequest -> ServerResponse
                        .ok()
                        .bodyValue(
                                new DinResponse<>(
                                        dinRequest.getDinHeader(),
                                        dinRequest.getDinBody()
                                )
                        )
                );
    }
}
