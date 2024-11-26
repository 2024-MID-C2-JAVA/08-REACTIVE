package com.bank.management;

import com.bank.management.gateway.EventBus;
import com.bank.management.generic.DomainEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitAdapter implements EventBus {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key.createUser}")
    private String routingKeyCreateUser;

    @Value("${rabbitmq.routing.key.createAccount}")
    private String routingKeyCreateAccount;

    @Value("${rabbitmq.routing.key.deleteAccount}")
    private String routingKeyDeleteAccount;

    @Value("${rabbitmq.routing.key.createCustomer}")
    private String routingKeyCreateCustomer;

    @Value("${rabbitmq.routing.key.deleteCustomer}")
    private String routingKeyDeleteCustomer;

    @Value("${rabbitmq.routing.key.deposit}")
    private String routingKeyDeposit;

    @Value("${rabbitmq.routing.key.withdraw}")
    private String routingKeyWithdraw;

    @Value("${rabbitmq.routing.key.purchase}")
    private String routingKeyPurchase;

    @Value("${encryption.initializationVector}")
    private String symmetricKey;

    @Value("${encryption.symmetricKey}")
    private String initializationVector;


    private final RabbitTemplate rabbitTemplate;


    public RabbitAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void createUserEvent(DomainEvent event) {
        rabbitTemplate.convertAndSend(exchangeName, routingKeyCreateUser, event);
    }
    @Override
    public void createAccountEvent(DomainEvent event) {
        rabbitTemplate.convertAndSend(exchangeName, routingKeyCreateAccount, event);
    }

    @Override
    public void depositEvent(DomainEvent event) {
        rabbitTemplate.convertAndSend(exchangeName, routingKeyDeposit, event);
    }

    @Override
    public void withdrawEvent(DomainEvent event) {
        rabbitTemplate.convertAndSend(exchangeName, routingKeyWithdraw, event);
    }

    @Override
    public void purchaseEvent(DomainEvent event) {
        rabbitTemplate.convertAndSend(exchangeName, routingKeyPurchase, event);
    }

    @Override
    public void deleteAccountEvent(DomainEvent event) {
        rabbitTemplate.convertAndSend(exchangeName, routingKeyDeleteAccount);
    }

    @Override
    public void deleteCustomerEvent(DomainEvent event) {
        rabbitTemplate.convertAndSend(exchangeName, routingKeyDeleteCustomer);
    }

    @Override
    public void createCustomerEvent(DomainEvent event) {
        rabbitTemplate.convertAndSend(exchangeName, routingKeyCreateCustomer, event);
    }


}
