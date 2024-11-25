package co.sofka.rabbitMq.listener;

import co.sofka.events.AccountCreatedEvent;

public interface AccountBusListener {
   void receiveEvent(AccountCreatedEvent accountCreatedEvent);
}
