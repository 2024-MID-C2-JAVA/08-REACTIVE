package co.com.sofka.cuentaflex.libs.domain.use_cases.query_handlers;

import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.ViewRepositoryPort;
import co.com.sofka.cuentaflex.libs.domain.model.accounts_views.CustomerView;
import co.com.sofka.cuentaflex.libs.domain.use_cases.QueryHandler;
import co.com.sofka.cuentaflex.libs.domain.model.accounts_views.queries.GetCustomerAccountsQuery;
import reactor.core.publisher.Mono;

public final class GetCustomerAccountsQueryHandler implements QueryHandler<GetCustomerAccountsQuery, Mono<CustomerView>> {
    private final ViewRepositoryPort viewRepositoryPort;

    public GetCustomerAccountsQueryHandler(ViewRepositoryPort viewRepositoryPort) {
        this.viewRepositoryPort = viewRepositoryPort;
    }

    @Override
    public Mono<CustomerView> apply(GetCustomerAccountsQuery getCustomerAccountsQuery) {
        return this.viewRepositoryPort.getCustomerView(getCustomerAccountsQuery.getCustomerId());
    }
}
