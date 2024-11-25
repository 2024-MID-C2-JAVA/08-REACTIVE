package co.sofka;


import co.sofka.data.entity.EventEntity;
import co.sofka.data.repository.MongoEventBankRepository;
import co.sofka.event.Notification;
import co.sofka.gateway.IEventBankRepository;
import co.sofka.gateway.IRabbitBus;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Repository
@AllArgsConstructor
public class EventBankRepository implements IEventBankRepository {

    private static final Logger logger = LoggerFactory.getLogger(EventBankRepository.class);


    private final ReactiveMongoTemplate mongoTemplate;

    private final MongoEventBankRepository repository;


    @Override
    @Transactional
    public Mono<Event> save(Event event) {

        EventEntity EventEntity = new EventEntity();
        EventEntity.setBody(event.getBody());
        EventEntity.setFecha(event.getFecha());
        EventEntity.setParentId(event.getParentId());
        EventEntity.setType(event.getType());
        EventEntity.setId(event.getId());

        logger.info("EventEntity: {}", EventEntity);

       return mongoTemplate.save(EventEntity,"event").map(
                eventEntity -> {
                     logger.info("EventEntity Save: {}", eventEntity);
                     return event;
                }
       );


    }


}
