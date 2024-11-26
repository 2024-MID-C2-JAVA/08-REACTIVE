package co.com.sofka.cuentaflex.libs.domain.model.accounts.events;

import co.com.sofka.cuentaflex.libs.domain.model.DomainEvent;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.Account;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.Balance;

public final class AccountCreatedEvent extends DomainEvent {
    private String customerId;
    private Balance initialBalance;

    public AccountCreatedEvent() {
        super(
                AccountCreatedEvent.class.getName(),
                Account.class.getName()
        );
    }

    public AccountCreatedEvent(String accountId, String customerId, Balance initialBalance) {
        super(
                AccountCreatedEvent.class.getName(),
                accountId,
                Account.class.getName()
        );

        this.customerId = customerId;
        this.initialBalance = initialBalance;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Balance getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(Balance initialBalance) {
        this.initialBalance = initialBalance;
    }
}
