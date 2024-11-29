package co.sofka.values.customer;

import co.sofka.generic.ValueObject;

public class Rol implements ValueObject<String> {

    private String rol;

    public Rol(String rol) {
        this.rol = rol;
    }

    @Override
    public String value() {
        return this.rol;
    }
}
