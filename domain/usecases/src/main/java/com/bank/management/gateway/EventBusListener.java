package com.bank.management.gateway;

import com.bank.management.generic.DomainEvent;

public interface EventBusListener {
    void createUserEvent(DomainEvent event);
    void createBankAccountEvent(DomainEvent event);

    @RabbitListener(queues = "#{@queueNameDeleteAccount}")
    void deleteBankAccountEvent(DomainEvent event);

    @RabbitListener(queues = "#{@queueNameDeleteCustomer}")
    void deleteCustomerEvent(DomainEvent event);

    @RabbitListener(queues = "#{@queueNameCreateCustomer}")
    void createCustomerEvent(DomainEvent event);

    void deposit(DomainEvent event);
    void purchase(DomainEvent event);
    void withdrawal(DomainEvent event);
}
