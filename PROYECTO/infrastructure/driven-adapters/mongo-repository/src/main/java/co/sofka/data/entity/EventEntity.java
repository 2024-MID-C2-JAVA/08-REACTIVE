package co.sofka.data.entity;

import co.sofka.event.CustomerCreated;
import co.sofka.generic.DomainEvent;
import co.sofka.serializer.JSONMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;


@Data
@Document(collection = "event")
@AllArgsConstructor
@NoArgsConstructor
public class EventEntity implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(EventEntity.class);

    private String aggregateRootId;

    private String type;

    private Date fecha;

    private String eventBody;


    @SneakyThrows
    public static String wrapEvent(DomainEvent domainEvent, JSONMapper eventSerializer){
        return eventSerializer.writeToJson(domainEvent);
    }

    public DomainEvent deserializeEvent(JSONMapper eventSerializer) {
        logger.info("Type Entity: {}", this.getType());
        try {
            return (DomainEvent) eventSerializer
                    .readFromJson(this.getEventBody(), Class.forName(this.getType()));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public <T> T convertJsonToObject(String jsonString, Class<T> clazz,ObjectMapper objectMapper) throws Exception {
        return objectMapper.readValue(jsonString, clazz);
    }

    public interface EventSerializer {
        <T extends DomainEvent> T deserialize(String aSerialization, final Class<?> aType);

        String serialize(DomainEvent object);
    }



}
