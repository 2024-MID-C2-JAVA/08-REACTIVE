package co.com.sofka.cuentaflex.libs.infrastructure.driven_adapters.rabbitmq_event_bus;

import co.com.sofka.cuentaflex.libs.domain.model.DomainEvent;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.messaging.EventBus;
import co.com.sofka.cuentaflex.libs.domain.ports.utils.encryption.AESCipher;
import co.com.sofka.cuentaflex.libs.domain.ports.utils.serializer.EventSerializer;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public final class EventBusAdapter implements EventBus {
    private final AESCipher aesCipher;
    private final String symmetricKey;
    private final String exchangeName;
    private final String routingKey;
    private final AmqpTemplate amqpTemplate;
    private final EventSerializer eventSerializer;

    public EventBusAdapter(
            AmqpTemplate amqpTemplate,
            AESCipher aesCipher,
            EventSerializer eventSerializer,
            @Value("${event-bus.encryption.symmetric-key}") String symmetricKey,
            @Value("${event-bus.queue.exchange}") String exchangeName,
            @Value("${event-bus.queue.routing-key}") String routingKey
    ) {
        this.amqpTemplate = amqpTemplate;
        this.aesCipher = aesCipher;
        this.eventSerializer = eventSerializer;
        this.symmetricKey = symmetricKey;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
    }


    @Override
    public void publish(DomainEvent event) {
        String iv = generateRandomIV();
        String jsonMessage = this.eventSerializer.serialize(event);
        String encryptedMessage = aesCipher.encrypt(jsonMessage, this.symmetricKey, iv);

        String finalMessage = String.format("%s:%s", iv, encryptedMessage);

        amqpTemplate.convertAndSend(
                this.exchangeName,
                this.routingKey,
                finalMessage
        );
    }

    private String generateRandomIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return Base64.getEncoder().encodeToString(iv).substring(0, 16);
    }
}
