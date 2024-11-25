package co.sofka.usecase.appEventBank;


import co.sofka.Event;
import co.sofka.dto.BankTransactionDepositSucursal;
import co.sofka.dto.BankTransactionDepositTransfer;
import co.sofka.event.Notification;
import co.sofka.gateway.IEventBankRepository;
import co.sofka.gateway.IRabbitBus;
import co.sofka.utils.ObjectToJsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class SaveEventTransactionDepositTransferenciaUseCase implements ISaveEventTransactionDepositTransferenciaService {

    private static final Logger logger = LoggerFactory.getLogger(SaveEventTransactionDepositTransferenciaUseCase.class);

    private final IEventBankRepository repository;

    private final IRabbitBus rabbitBus;

    public SaveEventTransactionDepositTransferenciaUseCase(IEventBankRepository repository, IRabbitBus rabbitBus) {
        this.repository = repository;
        this.rabbitBus = rabbitBus;
    }


    public Flux<Event> apply(Mono<BankTransactionDepositTransfer> request) {

        return request.flatMapIterable(item -> {
            logger.info("Customer created: {}", item);
            return List.of(item);
        }).map(item -> {
            Event event = new Event();
            try {
                event.setBody(ObjectToJsonConverter.convertObjectToJson(item));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            event.setFecha(LocalDateTime.now());
            event.setType("TransactionDepositTransferencia");
            event.setParentId(item.getCustomerReceiverId());
            event.setId(UUID.randomUUID().toString());


            Notification notification = new Notification();
            notification.setMessage(event.getBody());
            notification.setWhen(Instant.now());
            notification.setType("TransactionDepositTransferencia");
            notification.setUuid(event.getParentId());

            rabbitBus.send(notification);

            return event;
        }).flatMap(event -> {
            return repository.save(event);
        });
    }


}
