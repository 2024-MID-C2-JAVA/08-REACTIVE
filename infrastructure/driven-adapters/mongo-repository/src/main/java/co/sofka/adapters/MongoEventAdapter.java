package co.sofka.adapters;

import co.sofka.data.EventDocument;
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
        EventDocument eventDocument=new EventDocument();

        eventDocument.setAggregateRootId(domainEvent.getAggregateRootId());
        eventDocument.setAggregate(domainEvent.getAggregate());
        eventDocument.setVersionType(domainEvent.getVersionType());
        eventDocument.setType(domainEvent.getType());
        eventDocument.setUuid(domainEvent.getUuid());
        eventDocument.setWhen(domainEvent.getWhen());

        return template.save(eventDocument).thenReturn(domainEvent);
    }

}
