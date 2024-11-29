package co.sofka.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Document(collection = "Events")
public class EventDocument {
    @Id
    public  UUID uuid;
    public  Instant when;
    public  String type;
    private String aggregateRootId;
    private String aggregate;
    private Long versionType;
    private String body;

    public EventDocument() {
        this.when = Instant.now();
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

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setWhen(Instant when) {
        this.when = when;
    }

    public void setType(String type) {
        this.type = type;
    }
}
