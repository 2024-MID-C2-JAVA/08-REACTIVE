package com.bank.management.values.generic;

import com.bank.management.generic.ValueObject;

import java.util.Date;
import java.util.Objects;

public class CreatedAt implements ValueObject<Date> {

    private Date createdAt;

    public CreatedAt(Date createdAt) {
        if(createdAt == null) throw new IllegalArgumentException("createdAt must not be null");
        this.createdAt = Objects.requireNonNull(createdAt);
    }


    @Override
    public Date value() {
        return createdAt;
    }
    public static CreatedAt of(Date createdAt){
        return new CreatedAt(createdAt);
    }

}
