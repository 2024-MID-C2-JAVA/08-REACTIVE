package co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_views_webservice.handlers;

import co.com.sofka.cuentaflex.libs.domain.use_cases.queries.GetCustomerAccountsQuery;
import co.com.sofka.cuentaflex.libs.domain.use_cases.query_handlers.GetCustomerAccountsQueryHandler;
import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.din.DinRequest;
import co.com.sofka.cuentaflex.libs.infrastructure.entry_points.din.DinResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public final class GetCustomerAccountsHandler {
    private final GetCustomerAccountsQueryHandler getCustomerAccountsQueryHandler;

    public GetCustomerAccountsHandler(GetCustomerAccountsQueryHandler getCustomerAccountsQueryHandler) {
        this.getCustomerAccountsQueryHandler = getCustomerAccountsQueryHandler;
    }

    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(
                new ParameterizedTypeReference<DinRequest<GetCustomerAccountsQuery>>() {
                }
        ).flatMap(dinRequest -> getCustomerAccountsQueryHandler
                .apply(dinRequest.getDinBody())
                .map(customerView -> new DinResponse<>(dinRequest.getDinHeader(), customerView))
        ).flatMap(dinResponse -> ServerResponse
                .ok()
                .bodyValue(dinResponse)
        ).onErrorResume(e -> {
            e.printStackTrace(); // Imprime el error para depuración
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue("Invalid Request: " + e.getMessage());
        });
    }
}
