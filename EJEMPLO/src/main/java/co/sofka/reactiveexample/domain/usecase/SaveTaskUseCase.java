package co.sofka.reactiveexample.domain.usecase;

import co.sofka.reactiveexample.domain.model.Task;
import co.sofka.reactiveexample.domain.usecase.gateway.TaskRepository;
import reactor.core.publisher.Mono;

public class SaveTaskUseCase {

    private final TaskRepository repository;

    public SaveTaskUseCase(TaskRepository repository){
        this.repository = repository;
    }

    public Mono<Task> apply(Task task){
        return repository.save(task);
    }
}
