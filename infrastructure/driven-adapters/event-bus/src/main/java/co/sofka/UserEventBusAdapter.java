package co.sofka;

import co.sofka.events.CreateUserEvent;
import co.sofka.events.DomainEvent;
import co.sofka.rabbitMq.bus.UserEventBus;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserEventBusAdapter implements UserEventBus {

    private final RabbitTemplate template;

    @Value("${USER_ROUTING_KEY}")
    private String USER_ROUTING_KEY;

    @Value("${EXCHANGE_NAME}")
    private String EXCHANGE_NAME;

    public UserEventBusAdapter(RabbitTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<DomainEvent> publishEvent(CreateUserEvent createUserEvent) {
        template.convertAndSend(EXCHANGE_NAME, USER_ROUTING_KEY, createUserEvent);
        return Mono.just(createUserEvent);
    }
}
