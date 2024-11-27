package co.com.sofka.cuentaflex.libs.infrastructure.entry_points.mq_view_listener;

import co.com.sofka.cuentaflex.libs.domain.model.DomainEvent;
import co.com.sofka.cuentaflex.libs.domain.ports.utils.encryption.AESCipher;
import co.com.sofka.cuentaflex.libs.domain.ports.utils.serializer.EventSerializer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MqListener {
    private final EventSerializer eventSerializer;
    private final String symmetricKey;
    private final AESCipher aesCipher;
    private final EventHandlerDispatcher eventHandlerDispatcher;

    public MqListener(
            EventSerializer eventSerializer,
            @Value("${event-bus.encryption.symmetric-key}") String symmetricKey,
            AESCipher aesCipher,
            EventHandlerDispatcher eventHandlerDispatcher
    ) {
        this.eventSerializer = eventSerializer;
        this.symmetricKey = symmetricKey;
        this.aesCipher = aesCipher;
        this.eventHandlerDispatcher = eventHandlerDispatcher;
    }

    @RabbitListener(queues = "${event-bus.queue.name}")
    public void receive(String message) {
        String jsonMessage = message.split(":")[1];
        String iv = message.split(":")[0];
        String plainText = aesCipher.decrypt(jsonMessage, this.symmetricKey, iv);
        Notification notification = Notification.of(plainText, this.eventSerializer);
        DomainEvent event = null;
        try {
            event = (DomainEvent) this.eventSerializer.deserialize(notification.getBody(), Class.forName(notification.getType()));
            eventHandlerDispatcher.handle(event).subscribe();
        } catch (ClassNotFoundException _) {
        }
    }
}
