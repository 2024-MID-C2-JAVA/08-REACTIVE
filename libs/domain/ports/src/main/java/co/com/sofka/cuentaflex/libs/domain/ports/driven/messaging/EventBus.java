package co.com.sofka.cuentaflex.libs.domain.ports.driven.messaging;

import co.com.sofka.cuentaflex.libs.domain.model.DomainEvent;

public interface EventBus {
    void publish(DomainEvent event);
}
