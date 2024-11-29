package co.sofka.values.comun;

import co.sofka.generic.ValueObject;

public class Delete implements ValueObject<Boolean> {

    private Boolean value;

    public Delete(Boolean value) {
        this.value = value;
    }

    @Override
    public Boolean value() {
        return this.value;
    }
}
