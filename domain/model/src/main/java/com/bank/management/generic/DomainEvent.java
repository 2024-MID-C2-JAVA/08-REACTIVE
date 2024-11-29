package com.bank.management.generic;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public abstract class DomainEvent implements Serializable {

    public Instant when;
    public UUID uuid;
    public String type;
    private String aggregateRootId;
    private String aggregate;
    private Long versionType;

    public DomainEvent(final String type) {
        this.type = type;
        this.aggregate = "default";
        this.when = Instant.now();
        this.uuid = UUID.randomUUID();
        this.versionType = 1L;
    }

    public DomainEvent(Instant when, UUID uuid, String type, String aggregateRootId, String aggregate, Long versionType) {
        this.when = when;
        this.uuid = uuid;
        this.type = type;
        this.aggregateRootId = aggregateRootId;
        this.aggregate = aggregate;
        this.versionType = versionType;
    }

    public DomainEvent() {
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

    public String getAggregate() {
        return aggregate;
    }

    public void setAggregate(String aggregate) {
        this.aggregate = aggregate;
    }

    public Long getVersionType() {
        return versionType;
    }

    public Long versionType() {
        return versionType;
    }

    public void setVersionType(Long versionType) {
        this.versionType = versionType;
    }

    public String aggregateRootId() {
        return aggregateRootId;
    }

    public void setAggregateRootId(String aggregateRootId) {
        this.aggregateRootId = Objects.requireNonNull(aggregateRootId, "The aggregateRootId cannot be null");
    }

    public String aggregateName() {
        return aggregate;
    }

    public void setAggregateName(String aggregate) {
        this.aggregate = aggregate;
    }

    public Instant when() {
        return when;
    }

    public UUID uuid() {
        return uuid;
    }

    public String type() {
        return type;
    }
}

