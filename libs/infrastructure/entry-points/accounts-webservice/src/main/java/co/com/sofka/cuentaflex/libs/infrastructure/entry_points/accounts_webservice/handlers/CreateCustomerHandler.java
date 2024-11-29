package co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_webservice.handlers;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.commands.CreateCustomerCommand;
import co.com.sofka.cuentaflex.libs.domain.use_cases.command_handlers.CreateCustomerCommandHandler;
import co.com.sofka.cuentaflex.libs.infrastructure.utils.din.DinRequest;
import co.com.sofka.cuentaflex.libs.infrastructure.utils.din.DinResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public final class CreateCustomerHandler {
    private final CreateCustomerCommandHandler createCustomerCommandHandler;

    public CreateCustomerHandler(CreateCustomerCommandHandler createCustomerCommandHandler) {
        this.createCustomerCommandHandler = createCustomerCommandHandler;
    }

    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        return serverRequest
                .bodyToMono(
                        new ParameterizedTypeReference<DinRequest<CreateCustomerCommand>>() {
                        }
                )
                .flatMap(dinRequest -> createCustomerCommandHandler
                        .apply(dinRequest.getDinBody())
                        .then(Mono.just(dinRequest))
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
