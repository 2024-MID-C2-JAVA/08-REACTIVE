package co.com.sofka.cuentaflex.libs.domain.model.accounts.events;

import co.com.sofka.cuentaflex.libs.domain.model.DomainEvent;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.Customer;

import java.math.BigDecimal;

public final class AccountCreatedEvent extends DomainEvent {
    private String accountId;
    private int accountNumber;
    private BigDecimal initialBalance;

    public AccountCreatedEvent() {
        super(
                AccountCreatedEvent.class.getName(),
                Customer.class.getName()
        );
    }

    public AccountCreatedEvent(String accountId, String customerId, int accountNumber, BigDecimal initialBalance) {
        super(
                AccountCreatedEvent.class.getName(),
                customerId,
                Customer.class.getName()
        );

        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.initialBalance = initialBalance;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }
}
