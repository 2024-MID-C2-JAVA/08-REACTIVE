package co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_webservice.handlers;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.commands.CreateAccountCommand;
import co.com.sofka.cuentaflex.libs.domain.use_cases.command_handlers.CreateAccountCommandHandler;
import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_webservice.data.CreateAccountRequest;
import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.din.DinRequest;
import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.din.DinResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public final class CreateAccountHandler {
    private final CreateAccountCommandHandler createAccountCommandHandler;

    public CreateAccountHandler(CreateAccountCommandHandler createAccountCommandHandler) {
        this.createAccountCommandHandler = createAccountCommandHandler;
    }

    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(
                new ParameterizedTypeReference<DinRequest<CreateAccountRequest>>() {
                }
        ).flatMap(dinRequest -> {
            DinRequest<CreateAccountCommand> commandDinRequest = new DinRequest<>(dinRequest.getDinHeader(), new CreateAccountCommand(
                    dinRequest.getDinBody().getAccountId(),
                    dinRequest.getDinBody().getCustomerId(),
                    generateAccountNumberWithEpoch(),
                    dinRequest.getDinBody().getInitialBalance()
            ));
            return createAccountCommandHandler.apply(commandDinRequest.getDinBody()).then(Mono.just(dinRequest));
        }).flatMap(dinRequest -> ServerResponse
                .ok()
                .bodyValue(
                        new DinResponse<>(
                                dinRequest.getDinHeader(),
                                dinRequest.getDinBody()
                        )
                )
        );
    }

    private int generateAccountNumberWithEpoch() {
        return (int) (LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) * 1000) % Integer.MAX_VALUE;
    }
}
