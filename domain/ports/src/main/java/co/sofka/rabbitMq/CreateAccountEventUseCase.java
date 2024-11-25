package co.sofka.rabbitMq;

import co.sofka.commands.CreateAccountCommand;
import co.sofka.events.DomainEvent;
import reactor.core.publisher.Mono;

public interface CreateAccountEventUseCase {
    Mono<DomainEvent> publish(Mono<CreateAccountCommand> createAccountCommand);
}
