package co.com.sofka.cuentaflex.libs.infrastructure.entry_points.mq_view_listener;

import co.com.sofka.cuentaflex.libs.domain.model.DomainEvent;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.events.CustomerCreatedEvent;
import co.com.sofka.cuentaflex.libs.domain.use_cases.event_handlers.CustomerCreatedEventHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class EventHandlerDispatcher {
    private final CustomerCreatedEventHandler customerCreatedEventHandler;

    public EventHandlerDispatcher(CustomerCreatedEventHandler customerCreatedEventHandler) {
        this.customerCreatedEventHandler = customerCreatedEventHandler;
    }

    public Mono<Void> handle(DomainEvent event) {
        if(event instanceof CustomerCreatedEvent) {
            return customerCreatedEventHandler.apply((CustomerCreatedEvent) event);
        }

        return Mono.empty();
    }
}
