package com.bank.management.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.queue.name.createUser}")
    private String queueNameCreateUser;
    @Value("${rabbitmq.routing.key.createUser}")
    private String routingKeyCreateUser;

    @Value("${rabbitmq.queue.name.createAccount}")
    private String queueNameCreateAccount;
    @Value("${rabbitmq.routing.key.createAccount}")
    private String routingKeyCreateAccount;

    @Value("${rabbitmq.queue.name.deposit}")
    private String queueNameDeposit;
    @Value("${rabbitmq.routing.key.deposit}")
    private String routingKeyDeposit;

    @Value("${rabbitmq.queue.name.withdraw}")
    private String queueNameWithdraw;
    @Value("${rabbitmq.routing.key.withdraw}")
    private String routingKeyWithdraw;

    @Value("${rabbitmq.queue.name.purchase}")
    private String queueNamePurchase;
    @Value("${rabbitmq.routing.key.purchase}")
    private String routingKeyPurchase;

    @Bean
    public Queue createUserQueue() {
        return new Queue(queueNameCreateUser, true);
    }

    @Bean
    public Queue createAccountQueue() {
        return new Queue(queueNameCreateAccount, true);
    }

    @Bean
    public Queue depositQueue() {
        return new Queue(queueNameDeposit, true);
    }

    @Bean
    public Queue withdrawQueue() {
        return new Queue(queueNameWithdraw, true);
    }

    @Bean
    public Queue purchaseQueue() {
        return new Queue(queueNamePurchase, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Binding createUserBinding(Queue createUserQueue, TopicExchange exchange) {
        return BindingBuilder.bind(createUserQueue).to(exchange).with(routingKeyCreateUser);
    }

    @Bean
    public Binding createAccountBinding(Queue createAccountQueue, TopicExchange exchange) {
        return BindingBuilder.bind(createAccountQueue).to(exchange).with(routingKeyCreateAccount);
    }

    @Bean
    public Binding depositBinding(Queue depositQueue, TopicExchange exchange) {
        return BindingBuilder.bind(depositQueue).to(exchange).with(routingKeyDeposit);
    }

    @Bean
    public Binding withdrawBinding(Queue withdrawQueue, TopicExchange exchange) {
        return BindingBuilder.bind(withdrawQueue).to(exchange).with(routingKeyWithdraw);
    }

    @Bean
    public Binding purchaseBinding(Queue purchaseQueue, TopicExchange exchange) {
        return BindingBuilder.bind(purchaseQueue).to(exchange).with(routingKeyPurchase);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public AmqpTemplate rabbitTemplateBean(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
