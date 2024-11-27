package co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_views_webservice.handlers;

import co.com.sofka.cuentaflex.libs.domain.use_cases.queries.GetCustomerAccountQuery;
import co.com.sofka.cuentaflex.libs.domain.use_cases.query_handlers.GetCustomerAccountQueryHandler;
import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.din.DinRequest;
import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.din.DinResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public final class GetCustomerAccountHandler {
    private final GetCustomerAccountQueryHandler getCustomerAccountQueryHandler;

    public GetCustomerAccountHandler(GetCustomerAccountQueryHandler getCustomerAccountQueryHandler) {
        this.getCustomerAccountQueryHandler = getCustomerAccountQueryHandler;
    }

    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(
                        new ParameterizedTypeReference<DinRequest<GetCustomerAccountQuery>>() {
                        }
                ).flatMap(dinRequest -> getCustomerAccountQueryHandler
                        .apply(dinRequest.getDinBody())
                        .map(accountView -> new DinResponse<>(dinRequest.getDinHeader(), accountView)))
                .flatMap(dinResponse -> ServerResponse
                        .ok()
                        .bodyValue(dinResponse)
                );
    }
}
