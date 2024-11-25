package co.sofka.adapters;

import co.sofka.events.DomainEvent;
import co.sofka.rabbitMq.EventRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class MongoEventAdapter implements EventRepository {

    private final ReactiveMongoTemplate template;

    public MongoEventAdapter(ReactiveMongoTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<DomainEvent> save(DomainEvent domainEvent) {
        return template.save(domainEvent);
    }

}
