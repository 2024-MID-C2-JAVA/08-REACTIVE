package co.sofka;

import co.sofka.events.AccountCreatedEvent;
import co.sofka.events.CreateUserEvent;
import co.sofka.events.DomainEvent;
import co.sofka.events.TransactionCreatedEvent;
import co.sofka.rabbitMq.bus.EventBus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class EventBusAdapter implements EventBus {

    private final RabbitTemplate template;
    private final ObjectMapper objectMapper;

    @Value("${EXCHANGE_NAME}")
    private String EXCHANGE_NAME;
    @Value("${ACCOUNT_ROUTING_KEY}")
    private String ACCOUNT_ROUTING_KEY;
    @Value("${TRANSACTION_ROUTING_KEY}")
    private String TRANSACTION_ROUTING_KEY;
    @Value("${USER_ROUTING_KEY}")
    private String USER_ROUTING_KEY;

    public EventBusAdapter(RabbitTemplate template, ObjectMapper objectMapper) {
        this.template = template;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<TransactionCreatedEvent> publishTransactionEvent(TransactionCreatedEvent event) {
        try {
            String json=objectMapper.writeValueAsString(event);
            template.convertAndSend(EXCHANGE_NAME, TRANSACTION_ROUTING_KEY, json);
            return Mono.just(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Mono<DomainEvent> publishUserEvent(CreateUserEvent createUserEvent) {
        try {
            String json=objectMapper.writeValueAsString(createUserEvent);
            System.out.println("JSON: "+json);
            template.convertAndSend(EXCHANGE_NAME, USER_ROUTING_KEY, json);
            return Mono.just(createUserEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Mono<DomainEvent> publishAccountEvent(AccountCreatedEvent accountCreatedEvent) {
        try {
            String json=objectMapper.writeValueAsString(accountCreatedEvent);
            template.convertAndSend(EXCHANGE_NAME,ACCOUNT_ROUTING_KEY,json);
            return Mono.just(accountCreatedEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
