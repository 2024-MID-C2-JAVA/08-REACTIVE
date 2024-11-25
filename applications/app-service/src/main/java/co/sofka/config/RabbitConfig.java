package co.sofka.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

    @Value("${EXCHANGE_NAME}")
    private String EXCHANGE_NAME;
    @Value("${USER_ROUTING_KEY}")
    private String USER_ROUTING_KEY;
    @Value("${ACCOUNT_ROUTING_KEY}")
    private String ACCOUNT_ROUTING_KEY;
    @Value("${TRANSACTION_ROUTING_KEY}")
    private String TRANSACTION_ROUTING_KEY;
    @Value("${ACCOUNT_QUEUE}")
    private String ACCOUNT_QUEUE;
    @Value("${USER_QUEUE}")
    private String USER_QUEUE;
    @Value("${TRANSACTION_QUEUE}")
    private String TRANSACTION_QUEUE;

    @Bean
    public TopicExchange exchange(){return new TopicExchange(EXCHANGE_NAME);}

    public Queue queueUser(){return new Queue(USER_QUEUE,true);}
    @Bean
    public Queue queueAccount(){return new Queue(ACCOUNT_QUEUE,true);}

    public Queue queueTransaction(){return new Queue(TRANSACTION_QUEUE,true);}

    @Bean
    public Binding userBinding(Queue queue, TopicExchange exchange){
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(USER_ROUTING_KEY);
    }

    @Bean
    public Binding accountBinding(Queue queue, TopicExchange exchange){
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(ACCOUNT_ROUTING_KEY);
    }

    @Bean
    public Binding transactionBinding(Queue queue, TopicExchange exchange){
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(TRANSACTION_ROUTING_KEY);
    }
}
