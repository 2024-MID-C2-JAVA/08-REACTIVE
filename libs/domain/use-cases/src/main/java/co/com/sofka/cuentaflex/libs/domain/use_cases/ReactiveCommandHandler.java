package co.com.sofka.cuentaflex.libs.domain.use_cases;

import co.com.sofka.cuentaflex.libs.domain.model.Command;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface ReactiveCommandHandler<CommandType extends Command> extends Function<CommandType, Mono<Void>> {
}
