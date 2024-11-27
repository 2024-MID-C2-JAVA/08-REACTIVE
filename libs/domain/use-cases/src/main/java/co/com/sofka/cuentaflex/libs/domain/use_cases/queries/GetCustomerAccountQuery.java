package co.com.sofka.cuentaflex.libs.domain.use_cases.queries;

import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.data.AccountView;
import co.com.sofka.cuentaflex.libs.domain.use_cases.Query;
import reactor.core.publisher.Mono;

public final class GetCustomerAccountQuery extends Query<Mono<AccountView>> {
    private final String customerId;
    private final String accountId;

    public GetCustomerAccountQuery(String customerId, String accountId) {
        this.customerId = customerId;
        this.accountId = accountId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getAccountId() {
        return accountId;
    }
}
