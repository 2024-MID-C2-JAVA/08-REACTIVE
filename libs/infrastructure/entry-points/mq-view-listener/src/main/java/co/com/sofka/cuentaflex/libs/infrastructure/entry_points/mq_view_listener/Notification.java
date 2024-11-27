package co.com.sofka.cuentaflex.libs.infrastructure.entry_points.mq_view_listener;

import co.com.sofka.cuentaflex.libs.domain.ports.utils.serializer.EventSerializer;

import java.time.Instant;
import java.util.UUID;

public final class Notification{
    private final UUID uuid;
    private final String type;
    private final String body;
    private final Instant when;

    public Notification() {
        this.uuid = null;
        this.type = null;
        this.body = null;
        this.when = null;
    }

    public Notification(UUID uuid, String type, String body, Instant when) {
        this.uuid = uuid;
        this.type = type;
        this.body = body;
        this.when = when;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getType() {
        return type;
    }

    public String getBody() {
        return body;
    }

    public Instant getWhen() {
        return when;
    }

    public static Notification of(String message, EventSerializer serializer) {
        Notification notification = (Notification) serializer.deserialize(message, Notification.class);
        return new Notification(
                notification.getUuid(),
                notification.getType(),
                message,
                notification.getWhen()
        );
    }
}
