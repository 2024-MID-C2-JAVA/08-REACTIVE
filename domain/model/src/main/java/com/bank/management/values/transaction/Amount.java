package com.bank.management.values.transaction;

import com.bank.management.generic.ValueObject;

import java.math.BigDecimal;

public class Amount implements ValueObject<BigDecimal> {

    private BigDecimal amount;

    public Amount(BigDecimal amount) {
        this.amount = amount;
    }


    @Override
    public BigDecimal value() {
        return amount;
    }
    public static Amount of(BigDecimal amount){
        return new Amount(amount);
    }

}
