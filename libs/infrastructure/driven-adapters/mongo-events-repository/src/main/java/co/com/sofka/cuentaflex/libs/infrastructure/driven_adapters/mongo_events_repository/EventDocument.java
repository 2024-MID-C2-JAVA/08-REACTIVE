package co.com.sofka.cuentaflex.libs.infrastructure.driven_adapters.mongo_events_repository;

import co.com.sofka.cuentaflex.libs.domain.model.DomainEvent;
import co.com.sofka.cuentaflex.libs.domain.ports.utils.serializer.EventSerializer;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "events")
public class EventDocument {
    private String id;
    private String type;
    private String body;
    private Instant occurredOn;

    public EventDocument() {
    }

    public EventDocument(String id, String type, String body, Instant occurredOn) {
        this.id = id;
        this.type = type;
        this.body = body;
        this.occurredOn = occurredOn;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Instant getOccurredOn() {
        return this.occurredOn;
    }

    public void setOccurredOn(Instant occurredOn) {
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
