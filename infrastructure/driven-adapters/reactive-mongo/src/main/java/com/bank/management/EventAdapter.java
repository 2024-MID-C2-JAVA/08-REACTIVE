package com.bank.management;

import com.bank.management.data.EventDocument;
import com.bank.management.gateway.EventRepository;
import com.bank.management.generic.DomainEvent;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.Date;

@Component
public class EventAdapter implements EventRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final JSONMapper eventSerializer;

    public EventAdapter(ReactiveMongoTemplate reactiveMongoTemplate, JSONMapper eventSerializer) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.eventSerializer = eventSerializer;
    }

    @Override
    public Mono<DomainEvent> save(DomainEvent event) {
        EventDocument document = new EventDocument();
        document.setAggregateRootId(event.aggregateRootId());
        document.setEventBody(EventDocument.wrapEvent(event, eventSerializer));
        document.setOccurredOn(new Date());
        document.setTypeName(event.getClass().getTypeName());
        return reactiveMongoTemplate.save(document)
                .map(eventStored -> eventStored.deserializeEvent(eventSerializer));
    }

    @Override
    public Flux<DomainEvent> findById(String aggregateId) {
        return reactiveMongoTemplate.find(new Query(Criteria.where("aggregateRootId").is(aggregateId)), EventDocument.class)
                .sort(Comparator.comparing(EventDocument::getOccurredOn))
                .map(storedEvent -> storedEvent.deserializeEvent(eventSerializer));
    }
}
