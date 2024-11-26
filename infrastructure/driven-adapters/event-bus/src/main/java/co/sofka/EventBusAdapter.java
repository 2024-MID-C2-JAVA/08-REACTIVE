package co.sofka;

import co.sofka.events.AccountCreatedEvent;
import co.sofka.events.CreateUserEvent;
import co.sofka.events.DomainEvent;
import co.sofka.events.TransactionCreatedEvent;
import co.sofka.rabbitMq.bus.EventBus;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class EventBusAdapter implements EventBus {

    private final RabbitTemplate template;

    @Value("${EXCHANGE_NAME}")
    private String EXCHANGE_NAME;
    @Value("${ACCOUNT_ROUTING_KEY}")
    private String ACCOUNT_ROUTING_KEY;
    @Value("${TRANSACTION_ROUTING_KEY]")
    private String TRANSACTION_ROUTING_KEY;
    @Value("${USER_ROUTING_KEY}")
    private String USER_ROUTING_KEY;

    public EventBusAdapter(RabbitTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<TransactionCreatedEvent> publishTransactionEvent(TransactionCreatedEvent event) {
        template.convertAndSend(EXCHANGE_NAME, TRANSACTION_ROUTING_KEY, event);
        return Mono.just(event);
    }

    @Override
    public Mono<DomainEvent> publishUserEvent(CreateUserEvent createUserEvent) {
        template.convertAndSend(EXCHANGE_NAME, USER_ROUTING_KEY, createUserEvent);
        return Mono.just(createUserEvent);
    }

    @Override
    public Mono<DomainEvent> publishAccountEvent(AccountCreatedEvent accountCreatedEvent) {
        template.convertAndSend(EXCHANGE_NAME,ACCOUNT_ROUTING_KEY,accountCreatedEvent);
        return Mono.just(accountCreatedEvent);
    }
}
