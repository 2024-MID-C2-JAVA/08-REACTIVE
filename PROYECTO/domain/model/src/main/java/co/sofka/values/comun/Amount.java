package co.sofka.values.comun;

import co.sofka.generic.ValueObject;

import java.math.BigDecimal;

public class Amount implements ValueObject<BigDecimal> {

    private BigDecimal amount;

    public Amount(BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        this.amount = amount;
    }

    @Override
    public BigDecimal value() {
        return this.amount;
    }
}
