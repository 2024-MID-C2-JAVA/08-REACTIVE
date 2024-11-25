package co.sofka;

import co.sofka.events.DomainEvent;
import co.sofka.events.TransactionCreatedEvent;
import co.sofka.handler.TransactionHandler;
import co.sofka.rabbitMq.listener.TransactionBusListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TransactioEventListener implements TransactionBusListener {

    private final TransactionHandler transactionHandler;

    public TransactioEventListener(TransactionHandler transactionHandler) {
        this.transactionHandler = transactionHandler;
    }

    @Override
    @RabbitListener(queues = "queue.transaction")
    public void receive(TransactionCreatedEvent transactionCreatedEvent) {
        Transaction transaction=new Transaction();
        transaction.setAmount(transactionCreatedEvent.getAmount());
        transaction.setType(transactionCreatedEvent.getTypeTrans());
        transaction.setAccountId(transactionCreatedEvent.getCustomerId());
        transactionHandler.createTransaction(transaction).subscribe();
    }
}
