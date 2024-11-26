package com.bank.management.gateway;

import com.bank.management.generic.DomainEvent;

public interface EventBus {
    void createUserEvent(DomainEvent event);
    void createAccountEvent(DomainEvent event);
    void depositEvent(DomainEvent event);
    void withdrawEvent(DomainEvent event);
    void purchaseEvent(DomainEvent event);;
    void deleteAccountEvent(DomainEvent event);
    void deleteCustomerEvent(DomainEvent event);
    void createCustomerEvent(DomainEvent event);
}
