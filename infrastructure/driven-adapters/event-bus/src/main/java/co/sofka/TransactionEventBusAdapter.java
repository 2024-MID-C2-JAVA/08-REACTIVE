package co.sofka;

import co.sofka.events.TransactionCreatedEvent;
import co.sofka.rabbitMq.bus.TransactionEventBus;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TransactionEventBusAdapter implements TransactionEventBus {

    private final RabbitTemplate rabbitTemplate;
    @Value("${EXCHANGE_NAME}")
    private String EXCHANGE_NAME;
    @Value("${TRANSACTION_ROUTING_KEY]")
    private String TRANSACTION_ROUTING_KEY;
    public TransactionEventBusAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public Mono<TransactionCreatedEvent> publishEvent(TransactionCreatedEvent event) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, TRANSACTION_ROUTING_KEY, event);
        return Mono.just(event);
    }
}
