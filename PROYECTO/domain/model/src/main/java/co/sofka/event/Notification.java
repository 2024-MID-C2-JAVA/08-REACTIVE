package co.sofka.event;

import java.time.Instant;

public class Notification {

    private String message;

    private String uuid;

    private String type;

    private Instant when;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Instant getWhen() {
        return when;
    }

    public void setWhen(Instant when) {
        this.when = when;
    }
}
