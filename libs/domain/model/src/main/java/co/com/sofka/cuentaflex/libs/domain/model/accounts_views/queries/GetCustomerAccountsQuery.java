package co.com.sofka.cuentaflex.libs.domain.model.accounts_views.queries;

import co.com.sofka.cuentaflex.libs.domain.model.accounts_views.CustomerView;
import co.com.sofka.cuentaflex.libs.domain.model.Query;
import reactor.core.publisher.Mono;

public final class GetCustomerAccountsQuery extends Query<Mono<CustomerView>> {
    private final String customerId;

    public GetCustomerAccountsQuery() {
        this.customerId = null;
    }

    public GetCustomerAccountsQuery(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }
}
