package co.com.sofka.cuentaflex.libs.domain.model.accounts.commands;

import co.com.sofka.cuentaflex.libs.domain.model.Command;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.Balance;

public final class CreateAccountCommand extends Command {
    private final String accountId;
    private final String customerId;
    private final Balance initialBalance;

    public CreateAccountCommand(String accountId, String customerId, Balance initialBalance) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.initialBalance = initialBalance;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public Balance getInitialBalance() {
        return this.initialBalance;
    }
}
