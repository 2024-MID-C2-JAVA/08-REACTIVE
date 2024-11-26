package com.bank.management.gateway;

import com.bank.management.generic.DomainEvent;

public interface EventBusListener {
    void createUserEvent(DomainEvent event);
    void createBankAccountEvent(DomainEvent event);
    void deleteBankAccountEvent(DomainEvent event);
    void deleteCustomerEvent(DomainEvent event);
    void createCustomerEvent(DomainEvent event);
    void deposit(DomainEvent event);
    void purchase(DomainEvent event);
    void withdrawal(DomainEvent event);
}
