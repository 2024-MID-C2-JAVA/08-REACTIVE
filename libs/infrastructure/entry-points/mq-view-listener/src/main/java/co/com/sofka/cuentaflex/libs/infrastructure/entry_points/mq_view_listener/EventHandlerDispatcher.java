package co.com.sofka.cuentaflex.libs.infrastructure.entry_points.mq_view_listener;

import co.com.sofka.cuentaflex.libs.domain.model.DomainEvent;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.events.AccountCreatedEvent;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.events.CustomerCreatedEvent;
import co.com.sofka.cuentaflex.libs.domain.use_cases.event_handlers.AccountCreatedEventHandler;
import co.com.sofka.cuentaflex.libs.domain.use_cases.event_handlers.CustomerCreatedEventHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class EventHandlerDispatcher {
    private final CustomerCreatedEventHandler customerCreatedEventHandler;
    private final AccountCreatedEventHandler accountCreatedEventHandler;

    public EventHandlerDispatcher(
            CustomerCreatedEventHandler customerCreatedEventHandler,
            AccountCreatedEventHandler accountCreatedEventHandler
    ) {
        this.customerCreatedEventHandler = customerCreatedEventHandler;
        this.accountCreatedEventHandler = accountCreatedEventHandler;
    }

    public Mono<Void> handle(DomainEvent event) {
        if(event instanceof CustomerCreatedEvent) {
            return customerCreatedEventHandler.apply((CustomerCreatedEvent) event);
        }

        if (event instanceof AccountCreatedEvent) {
            return accountCreatedEventHandler.apply((AccountCreatedEvent) event);
        }

        return Mono.empty();
    }
}
