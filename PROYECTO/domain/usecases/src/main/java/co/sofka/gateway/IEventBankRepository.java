package co.sofka.gateway;

import co.sofka.Event;
import reactor.core.publisher.Mono;


public interface IEventBankRepository {
    Mono<Event> save(Event event);
}
