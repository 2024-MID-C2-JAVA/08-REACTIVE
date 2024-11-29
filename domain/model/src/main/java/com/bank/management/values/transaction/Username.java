package com.bank.management.values.transaction;

import com.bank.management.generic.ValueObject;

import java.util.Objects;

public class Username implements ValueObject<String> {

    private String username;

    public Username(String title) {
        this.username = Objects.requireNonNull(title);
    }


    @Override
    public String value() {
        return username;
    }
    public static Username of(String uuid){
        return new Username(uuid);
    }

}
