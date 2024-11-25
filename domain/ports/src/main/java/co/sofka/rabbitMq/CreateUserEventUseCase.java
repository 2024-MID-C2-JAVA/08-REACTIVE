package co.sofka.rabbitMq;

import co.sofka.commands.CreateUserCommand;
import co.sofka.events.CreateUserEvent;
import reactor.core.publisher.Mono;

public interface CreateUserEventUseCase {
  Mono<CreateUserEvent> publish(Mono<CreateUserCommand> createUserCommandMono);
}
