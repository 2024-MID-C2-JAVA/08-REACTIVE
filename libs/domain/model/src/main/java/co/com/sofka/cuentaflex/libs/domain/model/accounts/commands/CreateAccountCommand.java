package co.com.sofka.cuentaflex.libs.domain.model.accounts.commands;

import co.com.sofka.cuentaflex.libs.domain.model.Command;

import java.math.BigDecimal;

public final class CreateAccountCommand extends Command {
    private final String accountId;
    private final String customerId;
    private final int accountNumber;
    private final BigDecimal initialBalance;

    public CreateAccountCommand(String accountId, String customerId, int accountNumber, BigDecimal initialBalance) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.initialBalance = initialBalance;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public int getAccountNumber() {
        return this.accountNumber;
    }

    public BigDecimal getInitialBalance() {
        return this.initialBalance;
    }
}
