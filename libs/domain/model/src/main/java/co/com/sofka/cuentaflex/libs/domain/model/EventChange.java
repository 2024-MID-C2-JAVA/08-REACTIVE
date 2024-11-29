package co.com.sofka.cuentaflex.libs.domain.model;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.events.CustomerCreatedEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@SuppressWarnings("ALL")
public abstract class EventChange {
    protected Set<Consumer<? super DomainEvent>> behaviors = new HashSet<>();

    protected void apply(Consumer<? extends DomainEvent> changeEvent) {
        behaviors.add((Consumer<? super DomainEvent>) changeEvent);
    }
}
