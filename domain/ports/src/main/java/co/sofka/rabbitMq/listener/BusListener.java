package co.sofka.rabbitMq.listener;

import co.sofka.events.AccountCreatedEvent;
import co.sofka.events.CreateUserEvent;
import co.sofka.events.TransactionCreatedEvent;

public interface BusListener {
    void receiveAccountEvent(AccountCreatedEvent accountCreatedEvent);
    void receiveTransactionEvent(TransactionCreatedEvent transactionCreatedEvent);
    void receiveUserEvent(CreateUserEvent createUserEvent);
}
