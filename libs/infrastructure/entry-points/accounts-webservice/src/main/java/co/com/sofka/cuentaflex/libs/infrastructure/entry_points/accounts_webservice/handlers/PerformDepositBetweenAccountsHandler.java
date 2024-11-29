package co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_webservice.handlers;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.commands.PerformDepositBetweenAccountsCommand;
import co.com.sofka.cuentaflex.libs.domain.use_cases.command_handlers.PerformDepositBetweenAccountsCommandHandler;
import co.com.sofka.cuentaflex.libs.infrastructure.utils.din.DinRequest;
import co.com.sofka.cuentaflex.libs.infrastructure.utils.din.DinResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public final class PerformDepositBetweenAccountsHandler {
    private final PerformDepositBetweenAccountsCommandHandler performDepositBetweenAccountsCommandHandler;

    public PerformDepositBetweenAccountsHandler(PerformDepositBetweenAccountsCommandHandler performDepositBetweenAccountsCommandHandler) {
        this.performDepositBetweenAccountsCommandHandler = performDepositBetweenAccountsCommandHandler;
    }

    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(
                new ParameterizedTypeReference<DinRequest<PerformDepositBetweenAccountsCommand>>() {
                }
        ).flatMap(dinRequest -> performDepositBetweenAccountsCommandHandler
                .apply(dinRequest.getDinBody())
                .then(Mono.just(dinRequest))
        ).flatMap(dinRequest -> ServerResponse
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
