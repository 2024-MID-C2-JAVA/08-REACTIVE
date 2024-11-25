package co.sofka.adapters;

import co.sofka.events.DomainEvent;
import co.sofka.rabbitMq.EventViewRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class MongoEventsAdapter implements EventViewRepository {

    private final ReactiveMongoTemplate template;

    public MongoEventsAdapter(ReactiveMongoTemplate template) {
        this.template = template;
    }

    @Override
    public Flux<DomainEvent> getAll(DomainEvent domainEvent) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(domainEvent.getAggregateRootId()));
        return template.find(query,DomainEvent.class);
    }
}
