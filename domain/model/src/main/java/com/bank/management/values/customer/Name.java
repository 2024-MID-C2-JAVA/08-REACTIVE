package com.bank.management.values.customer;

import com.bank.management.generic.ValueObject;

import java.util.Objects;

public class Name implements ValueObject<String> {

    private String name;

    public Name(String title) {
        this.name = Objects.requireNonNull(title);
    }


    @Override
    public String value() {
        return name;
    }
    public static Name of(String uuid){
        return new Name(uuid);
    }

}
