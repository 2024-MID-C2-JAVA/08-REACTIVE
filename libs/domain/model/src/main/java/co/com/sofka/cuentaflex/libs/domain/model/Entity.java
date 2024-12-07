package co.com.sofka.cuentaflex.libs.domain.model;

import java.util.Objects;

public abstract class Entity<ID extends Identity> {
    private final ID id;

    public Entity(ID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(id, entity.id);
    }

    public ID getId() {
        return this.id;
    }
}
