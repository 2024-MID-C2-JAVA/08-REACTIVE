package co.sofka.values.transaction;

import co.sofka.generic.ValueObject;

public class TransactionRole implements ValueObject<String> {

    private String value;

    public TransactionRole(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
