package co.com.sofka.cuentaflex.libs.domain.model;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class ChangeEventSubscriber {
    private final List<DomainEvent> changes = new LinkedList<>();
    private final Map<String, AtomicLong> versions = new ConcurrentHashMap<>();
    private final Set<Consumer<? super DomainEvent>> observables = new HashSet<>();

    public List<DomainEvent> getChanges() {
        return this.changes;
    }

    public final ChangeApply appendChange(DomainEvent domainEvent) {
        this.changes.add(domainEvent);
        return () -> this.applyEvent(domainEvent);
    }

    public final void subscribe(EventChange eventChange) {
        this.observables.addAll(eventChange.behaviors);
    }

    public final void applyEvent(DomainEvent domainEvent) {
        observables.forEach(consumer -> {
            try {
                consumer.accept(domainEvent);
                var map = versions.get(domainEvent.getType());

            } catch (ClassCastException ignored) {
            }
        });
    }


    @FunctionalInterface
    public interface ChangeApply {
        void apply();
    }
}
