package co.sofka.reactiveexample.infrastructure.entry_point.reactive_web;

import co.sofka.reactiveexample.domain.model.Task;
import co.sofka.reactiveexample.domain.usecase.GetAllTasksUseCase;
import co.sofka.reactiveexample.domain.usecase.SaveTaskUseCase;
import co.sofka.reactiveexample.infrastructure.entry_point.reactive_web.data.TaskDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/task")
public class Web {

    private final SaveTaskUseCase saveTaskUseCase;
    private final GetAllTasksUseCase getAllTasksUseCase;

    public Web(SaveTaskUseCase saveTaskUseCase, GetAllTasksUseCase getAllTasksUseCase) {
        this.saveTaskUseCase = saveTaskUseCase;
        this.getAllTasksUseCase = getAllTasksUseCase;
    }

    @PostMapping
    public Mono<Task> createTask(@RequestBody TaskDTO taskDTO) {

        Task newTask = new Task(
                taskDTO.getId(),
                taskDTO.getDescription(),
                taskDTO.getCompleted()
        );

        return saveTaskUseCase.apply(newTask);
    }

    @GetMapping
    public Flux<Task> getAllTasks() {
        return getAllTasksUseCase.apply();
    }

}
