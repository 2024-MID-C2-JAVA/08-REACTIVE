package co.sofka.values.comun;

import co.sofka.generic.ValueObject;

public class CustumerTransaction implements ValueObject<String> {
    private String value;

    public CustumerTransaction(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
