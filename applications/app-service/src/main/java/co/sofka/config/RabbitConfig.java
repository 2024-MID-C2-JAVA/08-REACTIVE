package co.sofka.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

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
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange exchange(){return new TopicExchange(EXCHANGE_NAME);}

    @Bean
    public Queue queueUser(){return new Queue(USER_QUEUE,true);}

    @Bean
    public Queue queueAccount(){return new Queue(ACCOUNT_QUEUE,true);}

    @Bean
    public Queue queueTransaction(){return new Queue(TRANSACTION_QUEUE,true);}

    @Bean
    public Binding userBinding(Queue queueUser, TopicExchange exchange){
        return BindingBuilder
                .bind(queueUser)
                .to(exchange)
                .with(USER_ROUTING_KEY);
    }


    @Bean
    public Binding accountBinding(Queue queueAccount, TopicExchange exchange){
        return BindingBuilder
                .bind(queueAccount)
                .to(exchange)
                .with(ACCOUNT_ROUTING_KEY);
    }

    @Bean
    public Binding transactionBinding(Queue queueTransaction, TopicExchange exchange){
        return BindingBuilder
                .bind(queueTransaction)
                .to(exchange)
                .with(TRANSACTION_ROUTING_KEY);
    }
}
