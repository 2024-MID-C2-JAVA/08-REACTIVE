package com.bank.management.values.transaction;

import com.bank.management.generic.ValueObject;

import java.util.Objects;

public class AccountNumber implements ValueObject<String> {

    private String accountNumbe;

    public AccountNumber(String accountNumbe) {
        this.accountNumbe = Objects.requireNonNull(accountNumbe);
    }


    @Override
    public String value() {
        return accountNumbe;
    }
    public static AccountNumber of(String uuid){
        return new AccountNumber(uuid);
    }

}
