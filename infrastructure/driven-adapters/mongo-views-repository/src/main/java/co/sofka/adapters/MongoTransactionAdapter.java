package co.sofka.adapters;

import co.sofka.Transaction;
import co.sofka.data.TransactionDocument;
import co.sofka.data.UserDocument;
import co.sofka.out.TransactionRepository;
import co.sofka.out.TransactionViewsRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Repository
public class MongoTransactionAdapter implements TransactionViewsRepository {

    private final ReactiveMongoTemplate template;

    public MongoTransactionAdapter(ReactiveMongoTemplate template) {
        this.template = template;
    }

    @Override
    public Flux<Transaction> getAllTransactions(Transaction transaction) {
        Query query = new Query(Criteria.where("_id").is(transaction.getId())
                .and("customer.account_customer.is_deleted").is(false));

        return template.findOne(query, UserDocument.class)
                .flatMapMany(userDocument -> {
                    List<Transaction> list = userDocument.getCustomer()
                            .getAccount()
                            .getTransactions()
                            .stream()
                            .map(transactionDocument -> {
                                ZoneOffset zoneOffset = ZoneOffset.UTC;
                                OffsetDateTime offsetDateTime = transactionDocument.getTimeStamp().atOffset(zoneOffset);

                                return new Transaction(
                                        transactionDocument.getId(),
                                        transactionDocument.getAmount(),
                                        transactionDocument.getAmountCost(),
                                        transactionDocument.getTypeOfTransaction(),
                                        offsetDateTime
                                );
                            }).toList();
                    return Flux.fromIterable(list);
                });
    }

}
