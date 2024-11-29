package co.sofka.values.comun;

import co.sofka.generic.ValueObject;

public class AccountTransaction implements ValueObject<String> {
    private String value;

    public AccountTransaction(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
