package co.sofka.values.customer;

import co.sofka.generic.ValueObject;

public class Password implements ValueObject<String> {

    private String password;

    public Password(String password) {
        if(password.length() <= 5){
            throw new IllegalArgumentException("Password must have a least 5 characters");
        }
        this.password = password;
    }

    @Override
    public String value() {
        return this.password;
    }
}
