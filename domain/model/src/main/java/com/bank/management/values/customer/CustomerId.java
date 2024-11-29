package com.bank.management.values.customer;

import com.bank.management.generic.Identity;

public class CustomerId extends Identity {

    public CustomerId() {
    }

    private CustomerId(String uuid) {
        super(uuid);
    }


    public static CustomerId of(String uuid){
        return new CustomerId(uuid);
    }
}
