package co.com.sofka.cuentaflex.libs.domain.use_cases.exceptions;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.Amount;

public final class MinimumAmountNotReached extends RuntimeException {
    private final Amount amount;
    private final Amount minimumAmount;

    public MinimumAmountNotReached(Amount amount, Amount minimumAmount) {
        super("Minimum amount not reached for transaction: " + amount + " < " + minimumAmount);

        this.amount = amount;
        this.minimumAmount = minimumAmount;
    }

    public Amount getAmount() {
        return this.amount;
    }

    public Amount getMinimumAmount() {
        return this.minimumAmount;
    }
}
