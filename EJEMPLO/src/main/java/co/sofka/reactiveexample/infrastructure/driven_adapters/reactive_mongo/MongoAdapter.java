package co.sofka.reactiveexample.infrastructure.driven_adapters.reactive_mongo;

import co.sofka.reactiveexample.domain.model.Task;
import co.sofka.reactiveexample.domain.usecase.gateway.TaskRepository;
import co.sofka.reactiveexample.infrastructure.driven_adapters.reactive_mongo.data.TaskDocument;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class MongoAdapter implements TaskRepository {

    private final ReactiveMongoTemplate template;

    public MongoAdapter(ReactiveMongoTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Task> save(Task task) {

        TaskDocument taskDocument = new TaskDocument(
                task.getId(),
                task.getDescription(),
                task.getCompleted()
        );

        return template
                .save(taskDocument)
                .map(
                      taskSaved -> new Task(
                              taskSaved.getId(),
                              taskSaved.getDescription(),
                              taskSaved.getCompleted()
                      )
                );
    }

    @Override
    public Flux<Task> findAll() {
        return template.findAll(TaskDocument.class)
                .map(
                        taskDocument -> new Task(
                                taskDocument.getId(),
                                taskDocument.getDescription(),
                                taskDocument.getCompleted()
                        )
                );
    }
}
