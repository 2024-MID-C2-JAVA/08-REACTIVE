package com.bank.management.generic;

import java.io.Serializable;

public interface ValueObject<T> extends Serializable {
    T value();
}
