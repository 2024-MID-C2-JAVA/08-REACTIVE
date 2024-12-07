package co.com.sofka.cuentaflex.libs.infrastructure.driven_adapters.mongo_events_repository;

import co.com.sofka.cuentaflex.libs.domain.model.DomainEvent;
import co.com.sofka.cuentaflex.libs.domain.ports.utils.serializer.EventSerializer;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;

@Document(collection = "events")
public class EventDocument {
    private String id;
    private String type;
    private String aggregateRootId;
    private String aggregateName;
    private String body;
    private LocalDateTime occurredOn;

    public EventDocument() {
    }

    public EventDocument(String id, String type, String aggregateRootId, String aggregateName, String body, LocalDateTime occurredOn) {
        this.id = id;
        this.type = type;
        this.aggregateRootId = aggregateRootId;
        this.aggregateName = aggregateName;
        this.body = body;
        this.occurredOn = occurredOn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAggregateName() {
        return aggregateName;
    }

    public void setAggregateName(String aggregateName) {
        this.aggregateName = aggregateName;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }

    public void setOccurredOn(LocalDateTime occurredOn) {
        this.occurredOn = occurredOn;
    }

    public static String serializeEventBody(DomainEvent event, EventSerializer serializer) {
        return serializer.serialize(event);
    }

    public DomainEvent deserializeEventBody(String eventBody, EventSerializer serializer) {
        try {
            return (DomainEvent) serializer.deserialize(eventBody, Class.forName(this.getType()));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
