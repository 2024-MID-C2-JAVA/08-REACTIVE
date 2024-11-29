package co.com.sofka.cuentaflex.libs.domain.model;

import java.util.Objects;

public abstract class Identity implements ValueObject<String> {

    private final String id;

    public Identity() {
        this.id = "";
    }

    public Identity(String id) {
        this.id = id;
    }

    @Override
    public String getValue() {
        return this.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if(obj == null || this.getClass() != obj.getClass()) return false;
        Identity that = (Identity) obj;
        return Objects.equals(this.id, that.id);
    }
}
