package co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_webservice.handlers;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.commands.CreateCustomerCommand;
import co.com.sofka.cuentaflex.libs.domain.use_cases.command_handlers.CreateCustomerReactiveCommandHandler;
import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.din.DinRequest;
import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.din.DinResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class CreateCustomerHandler {
    private final CreateCustomerReactiveCommandHandler createCustomerReactiveCommandHandler;

    public CreateCustomerHandler(CreateCustomerReactiveCommandHandler createCustomerReactiveCommandHandler) {
        this.createCustomerReactiveCommandHandler = createCustomerReactiveCommandHandler;
    }

    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        return serverRequest
                .bodyToMono(
                        new ParameterizedTypeReference<DinRequest<CreateCustomerCommand>>() {
                        }
                )
                .flatMap(dinRequest -> createCustomerReactiveCommandHandler
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
