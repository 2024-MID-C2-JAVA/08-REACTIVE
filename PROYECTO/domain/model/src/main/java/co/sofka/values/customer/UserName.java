package co.sofka.values.customer;

import co.sofka.generic.ValueObject;

import java.util.Objects;

public class UserName implements ValueObject<String> {

    private String username;

    public UserName(String username) {
        if(username.length() <= 3){
            throw new IllegalArgumentException("Title must have a least 3 characters");
        }
        this.username = Objects.requireNonNull(username);
    }

    @Override
    public String value() {
        return this.username;
    }
}
