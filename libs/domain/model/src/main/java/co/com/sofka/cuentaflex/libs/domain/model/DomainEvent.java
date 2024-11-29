package co.com.sofka.cuentaflex.libs.domain.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class DomainEvent implements Serializable {
    private final LocalDateTime when;
    private final UUID uuid;
    private final String type;
    private String aggregateRootId;
    private String aggregateName;
    private final Long version;

    public DomainEvent(final String type, final String aggregateName) {
        this(type, null, aggregateName);
    }

    public DomainEvent(final String type, String aggregateRootId, final String aggregateName) {
        this.when = LocalDateTime.now();
        this.uuid = UUID.randomUUID();
        this.type = type;
        this.aggregateRootId = aggregateRootId;
        this.aggregateName = aggregateName;
        this.version = 1L;
    }

    public LocalDateTime getWhen() {
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

    public String getAggregateName() {
        return aggregateName;
    }

    public Long getVersion() {
        return version;
    }

    public void setAggregateRootId(String aggregateRootId) {
        this.aggregateRootId = aggregateRootId;
    }

    public void setAggregateName(String aggregateName) {
        this.aggregateName = aggregateName;
    }
}
