package co.sofka.values.transaction;

import co.sofka.generic.ValueObject;

public class TypeTransaction implements ValueObject<String> {

    private String value;

    public TypeTransaction(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
