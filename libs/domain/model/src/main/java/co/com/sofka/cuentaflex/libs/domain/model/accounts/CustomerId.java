package co.com.sofka.cuentaflex.libs.domain.model.accounts;

import co.com.sofka.cuentaflex.libs.domain.model.Identity;

public final class CustomerId extends Identity {
    public CustomerId() {
    }

    private CustomerId(String id) {
        super(id);
    }

    public static CustomerId of(String id) {
        return new CustomerId(id);
    }
}
