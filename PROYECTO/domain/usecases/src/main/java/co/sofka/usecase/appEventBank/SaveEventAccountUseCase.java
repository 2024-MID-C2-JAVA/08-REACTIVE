package co.sofka.usecase.appEventBank;


import co.sofka.Customer;
import co.sofka.commands.request.AccountCommand;
import co.sofka.gateway.IEventBankRepository;
import co.sofka.gateway.IRabbitBus;
import co.sofka.generic.DomainEvent;
import co.sofka.values.account.AccountId;
import co.sofka.values.account.Number;
import co.sofka.values.comun.Amount;
import co.sofka.values.customer.CustomerId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class SaveEventAccountUseCase implements ISaveEventAccountService {

    private static final Logger logger = LoggerFactory.getLogger(SaveEventAccountUseCase.class);

    private final IEventBankRepository repository;

    private final IRabbitBus rabbitBus;

    public SaveEventAccountUseCase(IEventBankRepository repository,IRabbitBus rabbitBus) {
        this.repository = repository;
        this.rabbitBus = rabbitBus;
    }

    public Flux<DomainEvent> apply(Mono<AccountCommand> item) {

        return item.flatMapMany(command -> repository.findById(command.getCustomerId())
                .collectList()
                .flatMapIterable(events -> {
            logger.info("Customer created: {}", events);

            Customer customer = Customer.from(CustomerId.of(command.getCustomerId()), events);


            customer.addAccount(
                    AccountId.of(command.uuid()),
                    new Number(command.getNumber()),
                    new Amount(command.getAmount())
            );

            return customer.getUncommittedChanges();
        }).map(customer -> {
            logger.info("Customer created: {}", customer);

            rabbitBus.send(customer);

            return customer;
        }).flatMap(event -> {
            return repository.save(event);
//            return Mono.just(Event);
        }));
    }


}
