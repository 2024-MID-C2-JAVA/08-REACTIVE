package co.com.sofka.cuentaflex.libs.domain.model;

import java.io.Serializable;

public interface ValueObject<T> extends Serializable {
    T getValue();
}
