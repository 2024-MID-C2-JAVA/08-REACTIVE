package co.com.sofka.cuentaflex.libs.domain.use_cases;

import co.com.sofka.cuentaflex.libs.domain.model.DomainEvent;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface ReactiveEventHandler<EventType extends DomainEvent> extends Function<EventType, Mono<Void>> {
}
