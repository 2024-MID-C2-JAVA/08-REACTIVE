package co.sofka;

import co.sofka.events.AccountCreatedEvent;
import co.sofka.events.CreateUserEvent;
import co.sofka.events.TransactionCreatedEvent;
import co.sofka.handler.AccountListenerHandler;
import co.sofka.handler.TransactionListenerHandler;
import co.sofka.handler.UserListenerHandler;
import co.sofka.rabbitMq.listener.BusListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class EventListener implements BusListener {

    private final AccountListenerHandler accountListenerHandler;
    private final TransactionListenerHandler transactionListenerHandler;
    private final UserListenerHandler userListenerHandler;
    private final ObjectMapper objectMapper;

    public EventListener(AccountListenerHandler accountListenerHandler, TransactionListenerHandler transactionListenerHandler, UserListenerHandler userListenerHandler, ObjectMapper objectMapper) {
        this.accountListenerHandler = accountListenerHandler;
        this.transactionListenerHandler = transactionListenerHandler;
        this.userListenerHandler = userListenerHandler;
        this.objectMapper = objectMapper;
    }


    @Override
    @RabbitListener(queues = "queue.account")
    public void receiveAccountEvent(String json) {
        try {
            AccountCreatedEvent accountCreatedEvent = objectMapper.readValue(json, AccountCreatedEvent.class);
            Account account=new Account();
            account.setAmount(accountCreatedEvent.getAmount());
            account.setNumber(accountCreatedEvent.getNumber());
            account.setCustomerId(accountCreatedEvent.getCustomerId());
            accountListenerHandler.createAccount(account).subscribe();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @RabbitListener(queues = "queue.transaction")
    public void receiveTransactionEvent(String json) {
        try {
            TransactionCreatedEvent transactionCreatedEvent = objectMapper.readValue(json, TransactionCreatedEvent.class);
            Transaction transaction=new Transaction();
            transaction.setAmount(transactionCreatedEvent.getAmount());
            transaction.setType(transactionCreatedEvent.getTypeTrans());
            transaction.setAccountId(transactionCreatedEvent.getCustomerId());
            transactionListenerHandler.createTransaction(transaction).subscribe();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @RabbitListener(queues = "queue.user")
    public void receiveUserEvent(String json) {
        try {
            System.out.println("JSON: "+json);
            CreateUserEvent createUserEvent = objectMapper.readValue(json, CreateUserEvent.class);
            UserRequest userRequest=new UserRequest();
            userRequest.setFirstname(createUserEvent.getFirstname());
            userRequest.setLastname(createUserEvent.getLastname());
            userRequest.setEmail(createUserEvent.getEmail());
            userRequest.setPassword(createUserEvent.getPassword());
            userRequest.setRole(createUserEvent.getRole());
            System.out.println("recibo el evento: "+createUserEvent);
            userListenerHandler.register(userRequest).subscribe();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
