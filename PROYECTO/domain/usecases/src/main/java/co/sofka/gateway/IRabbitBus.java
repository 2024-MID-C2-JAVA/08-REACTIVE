package co.sofka.gateway;

import co.sofka.Event;
import co.sofka.event.Notification;

public interface IRabbitBus {
    void send(Event event);
}
