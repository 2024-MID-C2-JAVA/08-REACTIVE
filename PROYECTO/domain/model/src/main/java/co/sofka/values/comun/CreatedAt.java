package co.sofka.values.comun;

import co.sofka.generic.ValueObject;

import java.time.LocalDateTime;

public class CreatedAt implements ValueObject<LocalDateTime> {

    private LocalDateTime value;

    public CreatedAt(LocalDateTime value) {
        this.value = value;
    }

    @Override
    public LocalDateTime value() {
        return this.value;
    }
}
