package com.bank.management.values.customer;

import com.bank.management.generic.ValueObject;

import java.util.Objects;

public class Lastname implements ValueObject<String> {

    private String lastname;

    public Lastname(String title) {
        this.lastname = Objects.requireNonNull(title);
    }


    @Override
    public String value() {
        return lastname;
    }
    public static Lastname of(String uuid){
        return new Lastname(uuid);
    }

}
