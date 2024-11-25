package co.sofka.rabbitMq.listener;

import co.sofka.events.CreateUserEvent;
import co.sofka.events.DomainEvent;
import reactor.core.publisher.Mono;

public interface UserBusListener {
    void receive(CreateUserEvent createUserEvent);
}
