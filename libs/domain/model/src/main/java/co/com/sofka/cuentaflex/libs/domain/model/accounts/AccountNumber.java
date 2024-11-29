package co.com.sofka.cuentaflex.libs.domain.model.accounts;

import co.com.sofka.cuentaflex.libs.domain.model.ValueObject;

import java.util.Objects;

public final class AccountNumber implements ValueObject<Integer> {
    private final Integer number;

    public AccountNumber(Integer number) {
        Objects.requireNonNull(number, "The account number cannot be null");

        if (number <= 0) {
            throw new IllegalArgumentException("Account number must be greater than 0");
        }

        this.number = number;
    }

    @Override
    public Integer getValue() {
        return this.number;
    }
}
