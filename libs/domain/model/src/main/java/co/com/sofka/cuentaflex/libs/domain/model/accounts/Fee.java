package co.com.sofka.cuentaflex.libs.domain.model.accounts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Fee {
    public static final Fee ZERO = new Fee(BigDecimal.ZERO);

    private final BigDecimal value;

    public Fee(BigDecimal value) {
        Objects.requireNonNull(value, "The fee cannot be null");

        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("The fee cannot be negative: " + value);
        }

        this.value = value.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "$" +  String.format("%.2f", this.value);
    }
}
