package co.sofka.values.customer;

import co.sofka.generic.Identity;

public class CustomerId extends Identity {

    public CustomerId() {
    }

    private CustomerId(String uuid) {
        super(uuid);
    }

    public static CustomerId of(String uuid){
        return new CustomerId(uuid);
    }
}
