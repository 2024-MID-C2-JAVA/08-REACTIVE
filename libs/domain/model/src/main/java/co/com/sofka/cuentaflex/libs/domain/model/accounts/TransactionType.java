package co.com.sofka.cuentaflex.libs.domain.model.accounts;

import java.util.Map;

public enum TransactionType {
    DEPOSIT_FROM_BRANCH(false, true),
    DEPOSIT_FROM_ATM(false, true),
    DEPOSIT_BETWEEN_ACCOUNTS(false, false),
    PURCHASE_IN_STORE(true, true),
    PURCHASE_ONLINE(true, true),
    WITHDRAW_FROM_ATM(true, true);

    private final boolean isDebit;
    private final boolean isUnidirectional;

    TransactionType(boolean isDebit, boolean isUnidirectional) {
        this.isDebit = isDebit;
        this.isUnidirectional = isUnidirectional;
    }

    public boolean isDebit() {
        return this.isDebit;
    }

    public boolean isUnidirectional() {
        return this.isUnidirectional;
    }
}
