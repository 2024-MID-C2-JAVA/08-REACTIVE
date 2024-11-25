package co.sofka.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Document(collection = "EventsLog")
public class EventLogDocument {
    @Id
    public final UUID uuid;
    public final Instant when;
    public final String type;
    private String aggregateRootId;
    private String aggregate;
    private Long versionType;
    private String body;

    public EventLogDocument(Instant when, UUID uuid, String type) {
        this.when = Instant.now();
        this.uuid = UUID.randomUUID();
        this.type = type;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Instant getWhen() {
        return when;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
