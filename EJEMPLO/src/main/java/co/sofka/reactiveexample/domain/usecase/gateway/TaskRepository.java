package co.sofka.reactiveexample.domain.usecase.gateway;

import co.sofka.reactiveexample.domain.model.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TaskRepository {
    Mono<Task> save(Task task);
    Flux<Task> findAll();
}
