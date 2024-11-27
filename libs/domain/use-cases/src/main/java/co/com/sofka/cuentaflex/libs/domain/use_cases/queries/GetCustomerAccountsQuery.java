package co.com.sofka.cuentaflex.libs.domain.use_cases.queries;

import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.data.CustomerView;
import co.com.sofka.cuentaflex.libs.domain.use_cases.Query;
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
