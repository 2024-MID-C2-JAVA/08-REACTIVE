package co.sofka.data.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Document(collection = "event")
public class EventEntity {

    @Id
    private String id;

    @Field(name = "parent_id")
    private String parentId;

    private String type;

    private LocalDateTime fecha;

    private String body;


}
