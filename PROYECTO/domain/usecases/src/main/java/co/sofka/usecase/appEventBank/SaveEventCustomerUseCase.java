package co.sofka.usecase.appEventBank;


import co.sofka.Customer;
import co.sofka.Event;
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

public class SaveEventCustomerUseCase implements ISaveEventCustomerService {

    private static final Logger logger = LoggerFactory.getLogger(SaveEventCustomerUseCase.class);

    private final IEventBankRepository repository;

    private final IRabbitBus rabbitBus;

    public SaveEventCustomerUseCase(IEventBankRepository repository,IRabbitBus rabbitBus) {
        this.repository = repository;
        this.rabbitBus = rabbitBus;
    }


    public Flux<Event> apply(Mono<Customer> item) {

        return item.flatMapIterable(customer -> {
            logger.info("Customer created: {}", customer);
            customer.setId(UUID.randomUUID().toString());
            return List.of(customer);
        }).map(customer -> {
            logger.info("Customer created: {}", customer);
            Event event = new Event();
            try {
                event.setBody(ObjectToJsonConverter.convertObjectToJson(customer));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            event.setFecha(LocalDateTime.now());
            event.setType("Customer Created");
            event.setParentId(customer.getId());
            event.setId(UUID.randomUUID().toString());

            rabbitBus.send(event);

            return event;
        }).flatMap(event -> {
            return repository.save(event);
//            return Mono.just(Event);
        });
    }


}
