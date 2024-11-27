package co.com.sofka.cuentaflex.libs.domain.use_cases.command_handlers;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.Balance;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.Customer;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.commands.CreateAccountCommand;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.events.AccountCreatedEvent;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.messaging.EventBus;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.EventRepositoryPort;
import co.com.sofka.cuentaflex.libs.domain.use_cases.ReactiveCommandHandler;
import co.com.sofka.cuentaflex.libs.domain.use_cases.exceptions.CustomerDoesNotExistsException;
import reactor.core.publisher.Mono;

public final class CreateAccountCommandHandler implements ReactiveCommandHandler<CreateAccountCommand> {
    private final EventRepositoryPort eventRepositoryPort;
    private final EventBus eventBus;

    public CreateAccountCommandHandler(EventRepositoryPort eventRepositoryPort, EventBus eventBus) {
        this.eventRepositoryPort = eventRepositoryPort;
        this.eventBus = eventBus;
    }

    @Override
    public Mono<Void> apply(CreateAccountCommand createAccountCommand) {
        return eventRepositoryPort.findByAggregateRootIdAndAggregateName(createAccountCommand.getCustomerId(), Customer.class.getName())
                .hasElements()
                .flatMap(exists -> exists
                        ? Mono.just(createAccountCommand)
                        : Mono.error(new CustomerDoesNotExistsException(createAccountCommand.getCustomerId()))
                )
                .map(command -> new AccountCreatedEvent(command.getAccountId(), command.getCustomerId(), command.getInitialBalance()))
                .map(event -> {
                    this.eventBus.publish(event);
                    return event;
                })
                .flatMap(this.eventRepositoryPort::save)
                .then();
    }
}
