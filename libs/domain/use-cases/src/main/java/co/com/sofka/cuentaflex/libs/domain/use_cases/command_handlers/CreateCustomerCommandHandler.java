package co.com.sofka.cuentaflex.libs.domain.use_cases.command_handlers;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.Customer;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.commands.CreateCustomerCommand;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.events.CustomerCreatedEvent;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.messaging.EventBus;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.EventRepositoryPort;
import co.com.sofka.cuentaflex.libs.domain.use_cases.ReactiveCommandHandler;
import co.com.sofka.cuentaflex.libs.domain.use_cases.exceptions.CustomerAlreadyExistsException;
import reactor.core.publisher.Mono;

public final class CreateCustomerCommandHandler implements ReactiveCommandHandler<CreateCustomerCommand> {
    private final EventRepositoryPort eventRepositoryPort;
    private final EventBus eventBus;

    public CreateCustomerCommandHandler(EventRepositoryPort eventRepositoryPort, EventBus eventBus) {
        this.eventRepositoryPort = eventRepositoryPort;
        this.eventBus = eventBus;
    }

    @Override
    public Mono<Void> apply(CreateCustomerCommand createCustomerCommand) {
        return eventRepositoryPort.findByAggregateRootIdAndAggregateName(createCustomerCommand.getCustomerId(), Customer.class.getName())
                .hasElements()
                .flatMap(exists ->
                        exists ? Mono.error(new CustomerAlreadyExistsException(createCustomerCommand.getCustomerId()))
                                : Mono.just(createCustomerCommand)
                )
                .map(
                        command -> new CustomerCreatedEvent(
                                command.getCustomerId(),
                                command.getFirstName(),
                                command.getLastName(),
                                command.getIdentification()
                        )
                )
                .map(event -> {
                    this.eventBus.publish(event);
                    return event;
                })
                .flatMap(this.eventRepositoryPort::save)
                .then();
    }
}
