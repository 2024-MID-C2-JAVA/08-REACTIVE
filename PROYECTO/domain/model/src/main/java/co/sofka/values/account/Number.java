package co.sofka.values.account;

import co.sofka.generic.ValueObject;

public class Number implements ValueObject<String> {

    private String number;

    public Number(String number) {
        if(number.length() <= 5){
            throw new IllegalArgumentException("Number must have a least 5 characters");
        }
        this.number = number;
    }

    @Override
    public String value() {
        return this.number;
    }
}
