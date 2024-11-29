package co.sofka.config;


import co.sofka.data.entity.CustomerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface JpaBanktransactionRepository extends ReactiveMongoRepository<CustomerEntity, String> {

    @Query("{ 'accounts.number' : ?0 }")
    CustomerEntity findByNumberAccount(String number);
}
