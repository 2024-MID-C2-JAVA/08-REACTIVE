package com.bank.management.values.account;

import com.bank.management.generic.ValueObject;

import java.util.Objects;

public class Number implements ValueObject<String> {

    private String number;

    public Number(String title) {
        if(title.length() <= 9){
            throw new IllegalArgumentException("Title must have a least 10 characters");
        }
        this.number = Objects.requireNonNull(title);
    }


    @Override
    public String value() {
        return number;
    }
    public static Number of(String uuid){
        return new Number(uuid);
    }

}
