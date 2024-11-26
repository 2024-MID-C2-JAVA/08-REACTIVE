package co.com.sofka.cuentaflex.apps.accounts;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    @Bean
    public TopicExchange exchange(@Value("${event-bus.queue.exchange}") String exchangeName) {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Queue queue(@Value("${event-bus.queue.name}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding binding(@Value("${event-bus.queue.routing-key}") String routingKey, Queue queue, TopicExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(routingKey);
    }
}
