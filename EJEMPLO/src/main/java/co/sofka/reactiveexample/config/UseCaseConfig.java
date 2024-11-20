package co.sofka.reactiveexample.config;

import co.sofka.reactiveexample.domain.usecase.GetAllTasksUseCase;
import co.sofka.reactiveexample.domain.usecase.SaveTaskUseCase;
import co.sofka.reactiveexample.domain.usecase.gateway.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public SaveTaskUseCase saveTaskUseCase(TaskRepository taskRepository) {
        return new SaveTaskUseCase(taskRepository);
    }

    @Bean
    public GetAllTasksUseCase getAllTasksUseCase(TaskRepository taskRepository) {
        return new GetAllTasksUseCase(taskRepository);
    }
}
