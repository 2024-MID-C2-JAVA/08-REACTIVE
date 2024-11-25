package com.bank.management.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Document(collection = "events")
public class EventDocument {

    @Id
    private String id;
    private Instant when;
    private UUID uuid;
    private String type;
    private String aggregateRootId;
    private String aggregate;
    private Long versionType;
    private String body;

    public EventDocument(Instant when, UUID uuid, String type, String aggregateRootId, String aggregate, Long versionType, String body) {
        this.when = when;
        this.uuid = uuid;
        this.type = type;
        this.aggregateRootId = aggregateRootId;
        this.aggregate = aggregate;
        this.versionType = versionType;
        this.body = body;
    }

    public EventDocument() {
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getWhen() {
        return when;
    }

    public void setWhen(Instant when) {
        this.when = when;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
