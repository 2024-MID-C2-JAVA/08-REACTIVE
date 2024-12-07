package co.com.sofka.cuentaflex.libs.domain.model;

import java.util.List;

public abstract class AggregateRoot<ID extends Identity> extends Entity<ID> {
    private final ChangeEventSubscriber changeEventSubscriber;

    public AggregateRoot(ID id) {
        super(id);
        this.changeEventSubscriber = new ChangeEventSubscriber();
    }

    protected ChangeEventSubscriber.ChangeApply appendChange(DomainEvent domainEvent) {
        return this.changeEventSubscriber.appendChange(domainEvent);
    }

    public List<DomainEvent> getUncommittedChanges() {
        return List.copyOf(changeEventSubscriber.getChanges());
    }

    protected final void subscribe(EventChange eventChange) {
        changeEventSubscriber.subscribe(eventChange);
    }

    public void markChangesAsCommitted() {
        changeEventSubscriber.getChanges().clear();
    }

    protected void applyEvent(DomainEvent domainEvent) {
        changeEventSubscriber.applyEvent(domainEvent);
    }
}
