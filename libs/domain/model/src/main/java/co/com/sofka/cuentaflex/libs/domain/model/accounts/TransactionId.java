package co.com.sofka.cuentaflex.libs.domain.model.accounts;

import co.com.sofka.cuentaflex.libs.domain.model.Identity;

public final class TransactionId extends Identity {
    public TransactionId() {
    }

    private TransactionId(String id) {
        super(id);
    }

    public static TransactionId of(String id) {
        return new TransactionId(id);
    }
}
