package co.sofka.data.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;

@Data
@Document(collection = "banktransaction")
public class CustomerEntity {

    @Id
    private String id;

    private String username;

    private String pwd;

    private String rol;

    @Field(name = "created_at")
    private LocalDate createdAt;

    @Field(name = "is_deleted")
    private Boolean isDeleted;

    private List<AccountEntity> accounts;




}
