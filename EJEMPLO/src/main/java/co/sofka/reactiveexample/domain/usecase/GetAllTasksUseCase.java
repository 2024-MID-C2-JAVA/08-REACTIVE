package co.sofka.reactiveexample.domain.usecase;

import co.sofka.reactiveexample.domain.model.Task;
import co.sofka.reactiveexample.domain.usecase.gateway.TaskRepository;
import reactor.core.publisher.Flux;

public class GetAllTasksUseCase {
    private final TaskRepository repository;

    public GetAllTasksUseCase(TaskRepository repository){
        this.repository = repository;
    }

    public Flux<Task> apply(){
        return repository.findAll();
    }
}
