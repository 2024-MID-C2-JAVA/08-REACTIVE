package com.bank.management.values.generic;

import com.bank.management.generic.ValueObject;

import java.util.Objects;

public class IsDeleted implements ValueObject<Boolean> {

    private Boolean isDeleted;

    public IsDeleted(Boolean isDeleted) {
        if(isDeleted == null){
            this.isDeleted = false;
        } else {
            this.isDeleted = Objects.requireNonNull(isDeleted);
        }
    }


    @Override
    public Boolean value() {
        return isDeleted;
    }
    public static IsDeleted of(Boolean isDeleted){
        return new IsDeleted(isDeleted);
    }

}
