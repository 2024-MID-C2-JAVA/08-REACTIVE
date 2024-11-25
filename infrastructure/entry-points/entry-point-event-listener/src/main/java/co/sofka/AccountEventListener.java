package co.sofka;

import co.sofka.events.AccountCreatedEvent;
import co.sofka.events.DomainEvent;
import co.sofka.handler.AccountHandler;
import co.sofka.rabbitMq.listener.AccountBusListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AccountEventListener implements AccountBusListener {

    private final AccountHandler accountHandler;

    public AccountEventListener(AccountHandler accountHandler) {
        this.accountHandler = accountHandler;
    }

    @Override
    @RabbitListener(queues = "queue.account")
    public void receiveEvent(AccountCreatedEvent accountCreatedEvent) {
        Account account = new Account();
        account.setAmount(accountCreatedEvent.getAmount());
        account.setNumber(accountCreatedEvent.getNumber());
        account.setCustomerId(accountCreatedEvent.getCustomerId());
        accountHandler.createAccount(account).subscribe();
    }
}
