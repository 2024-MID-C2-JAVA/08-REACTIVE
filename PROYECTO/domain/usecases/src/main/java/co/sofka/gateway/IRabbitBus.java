package co.sofka.gateway;

import co.sofka.Event;
import co.sofka.event.Notification;
import co.sofka.generic.DomainEvent;

public interface IRabbitBus {
    void send(DomainEvent event);
}
