package co.sofka.data.repository;

import co.sofka.data.entity.EventEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MongoEventBankRepository extends ReactiveMongoRepository<EventEntity, String> {
}
