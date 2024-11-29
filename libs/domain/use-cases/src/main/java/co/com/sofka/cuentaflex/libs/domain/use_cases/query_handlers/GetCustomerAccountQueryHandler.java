package co.com.sofka.cuentaflex.libs.domain.use_cases.query_handlers;

import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.ViewRepositoryPort;
import co.com.sofka.cuentaflex.libs.domain.model.accounts_views.AccountView;
import co.com.sofka.cuentaflex.libs.domain.use_cases.QueryHandler;
import co.com.sofka.cuentaflex.libs.domain.model.accounts_views.queries.GetCustomerAccountQuery;
import reactor.core.publisher.Mono;

public final class GetCustomerAccountQueryHandler implements QueryHandler<GetCustomerAccountQuery, Mono<AccountView>> {
    private final ViewRepositoryPort viewRepositoryPort;

    public GetCustomerAccountQueryHandler(ViewRepositoryPort viewRepositoryPort) {
        this.viewRepositoryPort = viewRepositoryPort;
    }

    @Override
    public Mono<AccountView> apply(GetCustomerAccountQuery getCustomerAccountQuery) {
        return this.viewRepositoryPort.getAccountView(
                getCustomerAccountQuery.getCustomerId(),
                getCustomerAccountQuery.getAccountId()
        );
    }
}
