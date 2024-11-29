package com.bank.management.values.transaction;

import com.bank.management.generic.ValueObject;

import java.util.Objects;

public class TypeTransaction implements ValueObject<String> {

    private String type;

    public TypeTransaction(String type) {
        this.type = Objects.requireNonNull(type);
    }


    @Override
    public String value() {
        return type;
    }
    public static TypeTransaction of(String type){
        return new TypeTransaction(type);
    }

}
