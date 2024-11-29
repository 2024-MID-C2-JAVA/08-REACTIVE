package com.bank.management.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.name.createUser}")
    private String queueNameCreateUser;

    @Value("${rabbitmq.queue.name.createAccount}")
    private String queueNameCreateAccount;

    @Value("${rabbitmq.queue.name.deleteAccount}")
    private String queueNameDeleteAccount;

    @Value("${rabbitmq.queue.name.createCustomer}")
    private String queueNameCreateCustomer;

    @Value("${rabbitmq.queue.name.deleteCustomer}")
    private String queueNameDeleteCustomer;

    @Value("${rabbitmq.queue.name.deposit}")
    private String queueNameDeposit;

    @Value("${rabbitmq.queue.name.withdraw}")
    private String queueNameWithdraw;

    @Value("${rabbitmq.queue.name.purchase}")
    private String queueNamePurchase;

    @Bean
    public String queueNameCreateUser() {
        return queueNameCreateUser;
    }

    @Bean
    public String queueNameCreateAccount() {
        return queueNameCreateAccount;
    }

    @Bean
    public String queueNameDeleteCustomer() {
        return queueNameDeleteCustomer;
    }

    @Bean
    public String queueNameCreateCustomer() {
        return queueNameCreateCustomer;
    }

    @Bean
    public String queueNameDeleteAccount() {
        return queueNameDeleteAccount;
    }

    @Bean
    public String queueNameDeposit() {
        return queueNameDeposit;
    }

    @Bean
    public String queueNameWithdraw() {
        return queueNameWithdraw;
    }
    @Bean
    public String queueNamePurchase() {
        return queueNamePurchase;
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
