package co.com.sofka.cuentaflex.libs.domain.model.accounts;

import co.com.sofka.cuentaflex.libs.domain.model.Identity;

public final class AccountId extends Identity {
    public AccountId() {
    }

    private AccountId(String id) {
        super(id);
    }

    public static AccountId of(String id) {
        return new AccountId(id);
    }
}
