package co.sofka;

import co.sofka.events.AccountCreatedEvent;
import co.sofka.events.CreateUserEvent;
import co.sofka.events.TransactionCreatedEvent;
import co.sofka.handler.AccountHandler;
import co.sofka.handler.TransactionHandler;
import co.sofka.handler.UserHandler;
import co.sofka.rabbitMq.listener.BusListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class EventListener implements BusListener {

    private final AccountHandler accountHandler;
    private final TransactionHandler transactionHandler;
    private final UserHandler userHandler;

    public EventListener(AccountHandler accountHandler, TransactionHandler transactionHandler, UserHandler userHandler) {
        this.accountHandler = accountHandler;
        this.transactionHandler = transactionHandler;
        this.userHandler = userHandler;
    }


    @Override
    @RabbitListener(queues = "queue.account")
    public void receiveAccountEvent(AccountCreatedEvent accountCreatedEvent) {
        Account account = new Account();
        account.setAmount(accountCreatedEvent.getAmount());
        account.setNumber(accountCreatedEvent.getNumber());
        account.setCustomerId(accountCreatedEvent.getCustomerId());
        accountHandler.createAccount(account).subscribe();
    }

    @Override
    @RabbitListener(queues = "queue.transaction")
    public void receiveTransactionEvent(TransactionCreatedEvent transactionCreatedEvent) {
        Transaction transaction=new Transaction();
        transaction.setAmount(transactionCreatedEvent.getAmount());
        transaction.setType(transactionCreatedEvent.getTypeTrans());
        transaction.setAccountId(transactionCreatedEvent.getCustomerId());
        transactionHandler.createTransaction(transaction).subscribe();
    }

    @Override
    @RabbitListener(queues = "queue.user")
    public void receiveUserEvent(CreateUserEvent createUserEvent) {
        UserRequest userRequest=new UserRequest();
        userRequest.setFirstname(createUserEvent.getFirstname());
        userRequest.setLastname(createUserEvent.getLastname());
        userRequest.setEmail(createUserEvent.getEmail());
        userRequest.setPassword(createUserEvent.getPassword());
        userRequest.setRole(createUserEvent.getRole());

        userHandler.register(userRequest).subscribe();
    }
}
