package co.com.sofka.cuentaflex.libs.infrastructure.driven_adapters.mongo_events_repository;

import co.com.sofka.cuentaflex.libs.domain.model.DomainEvent;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.EventRepositoryPort;
import co.com.sofka.cuentaflex.libs.domain.ports.utils.serializer.EventSerializer;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class EventRepositoryAdapter implements EventRepositoryPort {
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final EventSerializer eventSerializer;

    public EventRepositoryAdapter(ReactiveMongoTemplate reactiveMongoTemplate, EventSerializer eventSerializer) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.eventSerializer = eventSerializer;
    }

    @Override
    public Mono<Void> save(DomainEvent event) {
        EventDocument eventDocument = new EventDocument(
                event.getUuid().toString(),
                event.getType(),
                EventDocument.serializeEventBody(event, this.eventSerializer),
                event.getWhen()
        );
        return this.reactiveMongoTemplate.save(eventDocument).then();
    }

    @Override
    public Flux<DomainEvent> findByAggregateRootIdAndAggregateName(String aggregateRootId, String aggregateName) {
        return Flux.empty();
    }
}
