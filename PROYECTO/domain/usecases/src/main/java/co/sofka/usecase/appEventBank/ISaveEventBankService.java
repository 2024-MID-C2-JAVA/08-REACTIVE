package co.sofka.usecase.appEventBank;

import co.sofka.Event;
import reactor.core.publisher.Mono;

public interface ISaveEventBankService {
    Mono<Event> save(Mono<Event> event);
}
