package co.com.sofka.cuentaflex.libs.domain.model.accounts;

public final class InsufficientFundsExceptions extends RuntimeException {
    private final String accountId;
    private final Amount amount;
    private final Balance currentBalance;

    public InsufficientFundsExceptions(String accountId, Amount amount, Balance currentBalance) {
        super(
                "The account with the ID " + accountId
                + "has not the sufficient balance to process the debit of the amount of "
                + amount + ". Current balance: " + currentBalance
        );
        this.accountId = accountId;
        this.amount = amount;
        this.currentBalance = currentBalance;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public Amount getAmount() {
        return this.amount;
    }

    public Balance getCurrentBalance() {
        return this.currentBalance;
    }
}
