package co.sofka;

import co.sofka.events.AccountCreatedEvent;
import co.sofka.events.DomainEvent;
import co.sofka.rabbitMq.bus.AccountEventBus;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AccountEventBusAdapter implements AccountEventBus {

    private final RabbitTemplate rabbitTemplate;

    @Value("${EXCHANGE_NAME}")
    private String EXCHANGE_NAME;
    @Value("${ACCOUNT_ROUTING_KEY}")
    private String ACCOUNT_ROUTING_KEY;

    public AccountEventBusAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public Mono<DomainEvent> publishEvent(AccountCreatedEvent accountCreatedEvent) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME,ACCOUNT_ROUTING_KEY,accountCreatedEvent);
        return Mono.just(accountCreatedEvent);
    }
}
