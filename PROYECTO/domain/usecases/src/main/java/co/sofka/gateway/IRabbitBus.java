package co.sofka.gateway;

import co.sofka.event.Notification;

public interface IRabbitBus {
    void send(Notification notification);
}
