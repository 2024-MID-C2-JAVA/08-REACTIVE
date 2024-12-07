package co.com.sofka.cuentaflex.libs.domain.model.accounts;

import co.com.sofka.cuentaflex.libs.domain.model.ValueObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Amount implements ValueObject<BigDecimal> {

    private final BigDecimal value;

    public Amount(BigDecimal value) {
        Objects.requireNonNull(value, "The amount cannot be null");

        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("The amount must be greater than zero: " + value);
        }

        this.value = value.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getValue() {
        return this.value;
    }

    public Amount addFee(Fee fee) {
        return new Amount(this.value.add(fee.getValue()));
    }

    public Amount subtractFee(Fee fee) {
        return new Amount(this.value.subtract(fee.getValue()));
    }

    @Override
    public String toString() {
        return "$" + String.format("%.2f", this.value);
    }
}
