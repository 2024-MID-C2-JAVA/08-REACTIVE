package co.sofka.usecase.appEventBank;

import co.sofka.commands.request.AccountCommand;
import co.sofka.generic.DomainEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ISaveEventAccountService {
    Flux<DomainEvent> apply(Mono<AccountCommand> event);
}
