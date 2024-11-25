package co.sofka;


import co.sofka.data.entity.CustomerEntity;
import co.sofka.data.entity.TransactionAccountDetailEntity;
import co.sofka.data.entity.TransactionEntity;
import co.sofka.gateway.ITransactionAccountDetailRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class TransactionAccountDetailRepository implements ITransactionAccountDetailRepository {

    private static final Logger logger = LoggerFactory.getLogger(TransactionAccountDetailRepository.class);


    private final ReactiveMongoTemplate mongoTemplate;


    @Override
    public Mono<TransactionAccountDetail> save(TransactionAccountDetail id) {

        Flux<CustomerEntity> banktransaction = mongoTemplate.findAll(CustomerEntity.class, "banktransaction");

        banktransaction.map(item->{
            item.getAccounts().stream().forEach(accountEntity -> {
                if (accountEntity.getNumber().equals(id.getAccount().getNumber())){
                    TransactionAccountDetailEntity transactionAccountDetailEntity = new TransactionAccountDetailEntity();
                    transactionAccountDetailEntity.setTransactionRole(id.getTransactionRole());

                    TransactionEntity transactionEntity = new TransactionEntity();
                    transactionEntity.setAmountTransaction(id.getTransaction().getAmountTransaction());
                    transactionEntity.setTransactionCost(id.getTransaction().getTransactionCost());
                    transactionEntity.setTypeTransaction(id.getTransaction().getTypeTransaction());
                    transactionEntity.setTimeStamp(id.getTransaction().getTimeStamp().toLocalDate());


                    transactionAccountDetailEntity.setTransaction(transactionEntity);
                    if (accountEntity.getTransactionAccountDetailEntity()==null){
                        accountEntity.setTransactionAccountDetailEntity(new ArrayList<>());
                    }
                    accountEntity.getTransactionAccountDetailEntity().add(transactionAccountDetailEntity);
                    mongoTemplate.save(item);
                }
            });
            return item;
        });

        id.setId(UUID.randomUUID().toString());
        Transaction transaction = id.getTransaction();
        transaction.setId(UUID.randomUUID().toString());
        id.setTransaction(transaction);

        return Mono.just(id);
    }

    @Override
    public Flux<TransactionAccountDetail> getAll() {
        return null;
    }



}
