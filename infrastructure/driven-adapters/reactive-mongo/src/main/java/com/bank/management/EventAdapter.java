package com.bank.management;

import com.bank.management.data.EventDocument;
import com.bank.management.gateway.EventRepository;
import com.bank.management.generic.DomainEvent;
import com.bank.management.mapper.EventMapper;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class EventAdapter implements EventRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public EventAdapter(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Mono<DomainEvent> save(DomainEvent event) {
        EventDocument document = EventMapper.toDocument(event);
        return reactiveMongoTemplate.save(document, "events")
                .map(EventMapper::toDomain);
    }
}
