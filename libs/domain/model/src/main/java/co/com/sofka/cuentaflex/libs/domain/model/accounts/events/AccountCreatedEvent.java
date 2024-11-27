package co.com.sofka.cuentaflex.libs.domain.model.accounts.events;

import co.com.sofka.cuentaflex.libs.domain.model.DomainEvent;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.Balance;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.Customer;

import java.math.BigDecimal;

public final class AccountCreatedEvent extends DomainEvent {
    private String accountId;
    private String customerId;
    private BigDecimal initialBalance;

    public AccountCreatedEvent() {
        super(
                AccountCreatedEvent.class.getName(),
                Customer.class.getName()
        );
    }

    public AccountCreatedEvent(String accountId, String customerId, BigDecimal initialBalance) {
        super(
                AccountCreatedEvent.class.getName(),
                accountId,
                Customer.class.getName()
        );

        this.accountId = accountId;
        this.customerId = customerId;
        this.initialBalance = initialBalance;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }
}
