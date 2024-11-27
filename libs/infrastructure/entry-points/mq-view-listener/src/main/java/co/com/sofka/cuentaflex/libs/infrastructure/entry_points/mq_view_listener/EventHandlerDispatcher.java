package co.com.sofka.cuentaflex.libs.infrastructure.entry_points.mq_view_listener;

import co.com.sofka.cuentaflex.libs.domain.model.DomainEvent;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.events.*;
import co.com.sofka.cuentaflex.libs.domain.use_cases.ReactiveEventHandler;
import co.com.sofka.cuentaflex.libs.domain.use_cases.event_handlers.*;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class EventHandlerDispatcher {

    private final Map<Class<? extends DomainEvent>, ReactiveEventHandler<? extends DomainEvent>> eventHandlers;

    public EventHandlerDispatcher(
            CustomerCreatedEventHandler customerCreatedEventHandler,
            AccountCreatedEventHandler accountCreatedEventHandler,
            UnidirectionalTransactionPerformedEventHandler unidirectionalTransactionPerformedEventHandler,
            FundsCreditedInDepositBetweenAccountsEventHandler fundsCreditedInDepositBetweenAccountsEventHandler,
            FundsDebitedInDepositBetweenAccountsEventHandler fundsDebitedInDepositBetweenAccountsEventHandler
    ) {
        this.eventHandlers = Map.of(
                CustomerCreatedEvent.class, customerCreatedEventHandler,
                AccountCreatedEvent.class, accountCreatedEventHandler,
                UnidirectionalTransactionPerformedEvent.class, unidirectionalTransactionPerformedEventHandler,
                FundsCreditedInDepositBetweenAccountsEvent.class, fundsCreditedInDepositBetweenAccountsEventHandler,
                FundsDebitedInDepositBetweenAccountsEvent.class, fundsDebitedInDepositBetweenAccountsEventHandler
        );
    }

    @SuppressWarnings("unchecked")
    public Mono<Void> handle(DomainEvent event) {
        ReactiveEventHandler<DomainEvent> eventHandler = (ReactiveEventHandler<DomainEvent>) eventHandlers.get(event.getClass());

        if (eventHandler != null) {
            return eventHandler.apply(event);
        }

        return Mono.empty();
    }
}