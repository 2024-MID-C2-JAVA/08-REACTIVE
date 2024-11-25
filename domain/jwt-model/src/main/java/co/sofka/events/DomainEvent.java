package co.sofka.events;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public abstract class DomainEvent implements Serializable {

    public final Instant when;
    public final UUID uuid;
    public final String type;
    private String aggregateRootId;
    private String aggregate;
    private Long versionType;

    protected DomainEvent(String aggregate, String type) {
        this.when = Instant.now();
        this.uuid = UUID.randomUUID();
        this.type = type;
        this.aggregate = aggregate;
    }

    public DomainEvent(Instant when, UUID uuid, String type) {
        this.when = when;
        this.uuid = uuid;
        this.type = type;
    }

    public Instant getWhen() {
        return when;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getType() {
        return type;
    }

    public String getAggregateRootId() {
        return aggregateRootId;
    }

    public void setAggregateRootId(String aggregateRootId) {
        this.aggregateRootId = aggregateRootId;
    }

    public String getAggregate() {
        return aggregate;
    }

    public void setAggregate(String aggregate) {
        this.aggregate = aggregate;
    }

    public Long getVersionType() {
        return versionType;
    }

    public void setVersionType(Long versionType) {
        this.versionType = versionType;
    }
}
