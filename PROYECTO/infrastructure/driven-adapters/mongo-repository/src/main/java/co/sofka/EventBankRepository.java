package co.sofka;


import co.sofka.data.entity.EventEntity;
import co.sofka.event.CustomerCreated;
import co.sofka.gateway.IEventBankRepository;
import co.sofka.generic.DomainEvent;
import co.sofka.serializer.JSONMapper;
import co.sofka.serializer.JsonToObjectConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.Date;

@Repository
@AllArgsConstructor
public class EventBankRepository implements IEventBankRepository {

    private static final Logger logger = LoggerFactory.getLogger(EventBankRepository.class);


    private final ReactiveMongoTemplate mongoTemplate;

    private final JSONMapper eventSerializer;

    @SneakyThrows
    @Override
    @Transactional
    public Mono<DomainEvent> save(DomainEvent event) {

        EventEntity EventEntity = new EventEntity();
        EventEntity.setEventBody(EventEntity.wrapEvent(event, eventSerializer));
        EventEntity.setFecha(new Date());
        EventEntity.setAggregateRootId(event.aggregateRootId());
        EventEntity.setType(event.getClass().getTypeName());

        logger.info("Entidad : {} {}", EventEntity, event.getClass().getTypeName());

        logger.info("EventEntity: {}", EventEntity.getEventBody());
        return mongoTemplate.save(EventEntity)
                .map(eventStored -> {
//                    try {
//                        CustomerCreated customerCreated = JsonToObjectConverter.convertJsonToObject(eventStored.getEventBody(), CustomerCreated.class);
//
//                        logger.info("CustomerCreated save: {}", customerCreated);
//
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
                    return eventStored.deserializeEvent(eventSerializer);
                });

//       return mongoTemplate.save(EventEntity,"event").map(
//                eventEntity -> {
//                     logger.info("EventEntity Save: {}", eventEntity);
//                     return event;
//                }
//       );


    }


    @Override
    public Flux<DomainEvent> findById(String aggregateId) {
        return mongoTemplate.find(new Query(Criteria.where("aggregateRootId").is(aggregateId)), EventEntity.class)
                .sort(Comparator.comparing(EventEntity::getFecha))
                .map(storedEvent -> {
                    logger.info("StoredEvent: {}", storedEvent.getEventBody());
                        return storedEvent.deserializeEvent(eventSerializer); //JsonToObjectConverter.convertJsonToObject(storedEvent.getEventBody(),DomainEvent.class);
                });
    }



    public static <T> T convertJsonToObject(String jsonString, Class<T> clazz,ObjectMapper objectMapper) throws Exception {
        return objectMapper.readValue(jsonString, clazz);
    }


}
