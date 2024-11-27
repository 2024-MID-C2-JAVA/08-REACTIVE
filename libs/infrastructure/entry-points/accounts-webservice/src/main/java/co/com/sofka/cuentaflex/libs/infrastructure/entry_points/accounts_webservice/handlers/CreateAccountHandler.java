package co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_webservice.handlers;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.commands.CreateAccountCommand;
import co.com.sofka.cuentaflex.libs.domain.use_cases.command_handlers.CreateAccountCommandHandler;
import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.din.DinRequest;
import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.din.DinResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public final class CreateAccountHandler {
    private final CreateAccountCommandHandler createAccountCommandHandler;

    public CreateAccountHandler(CreateAccountCommandHandler createAccountCommandHandler) {
        this.createAccountCommandHandler = createAccountCommandHandler;
    }

    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(
                new ParameterizedTypeReference<DinRequest<CreateAccountCommand>>() {
                }
        ).flatMap(dinRequest -> createAccountCommandHandler.apply(dinRequest.getDinBody())
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
