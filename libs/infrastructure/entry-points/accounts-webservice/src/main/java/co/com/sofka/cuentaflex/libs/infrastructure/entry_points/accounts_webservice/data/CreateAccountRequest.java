package co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_webservice.data;

import java.math.BigDecimal;

public class CreateAccountRequest {
    private final String accountId;
    private final String customerId;
    private final BigDecimal initialBalance;

    public CreateAccountRequest(String accountId, String customerId, BigDecimal initialBalance) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.initialBalance = initialBalance;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }
}
