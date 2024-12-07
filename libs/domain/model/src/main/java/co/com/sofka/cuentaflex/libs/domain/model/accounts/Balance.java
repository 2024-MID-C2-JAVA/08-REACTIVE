package co.com.sofka.cuentaflex.libs.domain.model.accounts;

import co.com.sofka.cuentaflex.libs.domain.model.ValueObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Balance implements ValueObject<BigDecimal> {
    private final BigDecimal value;

    public Balance(BigDecimal value) {
        Objects.requireNonNull(value, "The balance cannot be null");

        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("The balance cannot be negative: " + value);
        }

        this.value = value.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getValue() {
        return this.value;
    }

    public Balance add(Amount amount) {

        return new Balance(value.add(amount.getValue()));
    }

    public Balance subtract(Amount amount) {
        return new Balance(value.subtract(amount.getValue()));
    }

    @Override
    public String toString() {
        return "$" +  String.format("%.2f", this.value);
    }
}
