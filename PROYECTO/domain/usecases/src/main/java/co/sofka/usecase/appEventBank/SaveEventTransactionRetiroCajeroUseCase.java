package co.sofka.usecase.appEventBank;


import co.sofka.Customer;
import co.sofka.commands.request.BankTransactionWithdrawFromATM;
import co.sofka.gateway.IEventBankRepository;
import co.sofka.gateway.IRabbitBus;
import co.sofka.generic.DomainEvent;
import co.sofka.values.comun.AccountTransaction;
import co.sofka.values.comun.Amount;
import co.sofka.values.comun.CustumerTransaction;
import co.sofka.values.customer.CustomerId;
import co.sofka.values.transaction.TransactionId;
import co.sofka.values.transaction.TransactionRole;
import co.sofka.values.transaction.TypeTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class SaveEventTransactionRetiroCajeroUseCase implements ISaveEventTransactionRetiroCajeroService {

    private static final Logger logger = LoggerFactory.getLogger(SaveEventTransactionRetiroCajeroUseCase.class);

    private final IEventBankRepository repository;

    private final IRabbitBus rabbitBus;

    public SaveEventTransactionRetiroCajeroUseCase(IEventBankRepository repository, IRabbitBus rabbitBus) {
        this.repository = repository;
        this.rabbitBus = rabbitBus;
    }


    public Flux<DomainEvent> apply(Mono<BankTransactionWithdrawFromATM> request) {



        return request.flatMapMany(command -> repository.findById(command.getCustomerId())
                .collectList()
                .flatMapIterable(events -> {
                    logger.info("Transaction TransactionRetiroCajero created: {}", events);

                    Customer customer = Customer.from(CustomerId.of(command.getCustomerId()), events);

                    customer.addAccountTransaction(
                            TransactionId.of(command.uuid()),

                            new CustumerTransaction(command.getCustumerTransactionSenderId()),
                            new CustumerTransaction(command.getCustumerTransactionResiverId()),
                            new AccountTransaction(command.getNumberAccountTransactionSenderId()),
                            new AccountTransaction(command.getNumberAccountTransactionResiverId()),
                            new Amount(command.getAmountTransaction()),
                            new Amount(new BigDecimal(2)),
                            new TypeTransaction("TransactionRetiroCajero"),
                            new TransactionRole("Parrol")
                    );

                    return customer.getUncommittedChanges();
                }).map(customer -> {
                    logger.info("Transaction TransactionRetiroCajero created: {}", customer);


                    rabbitBus.send(customer);

                    return customer;
                }).flatMap(event -> {
                    return repository.save(event);
                }));


//        return request.flatMapIterable(item -> {
//            logger.info("TransactionRetiroCajero created: {}", item);
//            return List.of(item);
//        }).map(item -> {
//            Event event = new Event();
//            try {
//                event.setBody(ObjectToJsonConverter.convertObjectToJson(item));
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            event.setFecha(LocalDateTime.now());
//            event.setType("TransactionRetiroCajero");
//            event.setParentId(item.getCustomerId());
//            event.setId(UUID.randomUUID().toString());
//
//
//            rabbitBus.send(event);
//
//            return event;
//        }).flatMap(event -> {
//            return repository.save(event);
//        });
    }


}
