package co.sofka.usecase.appEventBank;


import co.sofka.Customer;
import co.sofka.commands.request.CustomerCommand;
import co.sofka.gateway.IEventBankRepository;
import co.sofka.gateway.IRabbitBus;
import co.sofka.generic.DomainEvent;
import co.sofka.values.customer.CustomerId;
import co.sofka.values.customer.Password;
import co.sofka.values.customer.Rol;
import co.sofka.values.customer.UserName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class SaveEventCustomerUseCase implements ISaveEventCustomerService {

    private static final Logger logger = LoggerFactory.getLogger(SaveEventCustomerUseCase.class);

    private final IEventBankRepository repository;

    private final IRabbitBus rabbitBus;

    public SaveEventCustomerUseCase(IEventBankRepository repository,IRabbitBus rabbitBus) {
        this.repository = repository;
        this.rabbitBus = rabbitBus;
    }


    public Flux<DomainEvent> apply(Mono<CustomerCommand> item) {

        return item.flatMapIterable(command -> {
            logger.info("Customer created: {}", command);
//            customer.setId(UUID.randomUUID().toString());
            Customer customer = new Customer(
                    CustomerId.of(command.uuid()),
                    new UserName(command.getUserName()),
                    new Password(command.getPwd()),
                    new Rol(command.getRol())
                    );
            return customer.getUncommittedChanges();
        }).map(customer -> {
            logger.info("Customer created: {}", customer);
//            Event event = new Event();
//            try {
//                event.setBody(ObjectToJsonConverter.convertObjectToJson(customer));
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            event.setFecha(LocalDateTime.now());
//            event.setType("Customer Created");
//            event.setParentId(customer.getId());
//            event.setId(UUID.randomUUID().toString());



            rabbitBus.send(customer);

            return customer;
        }).flatMap(event -> {
            return repository.save(event);
//            return Mono.just(Event);
        });
    }


}
