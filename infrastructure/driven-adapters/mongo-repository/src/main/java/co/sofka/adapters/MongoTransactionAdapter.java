package co.sofka.adapters;

import co.sofka.Transaction;
import co.sofka.data.TransactionDocument;
import co.sofka.data.UserDocument;
import co.sofka.out.TransactionRepository;
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
public class MongoTransactionAdapter implements TransactionRepository {

    private final ReactiveMongoTemplate template;

    public MongoTransactionAdapter(ReactiveMongoTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Transaction> createTransaction(Transaction transaction) {
        return template.findById(transaction.getAccountId(), UserDocument.class)
                .flatMap(userDocument -> {
                    TransactionDocument transactionDocument = new TransactionDocument();
                    transactionDocument.setAmount(transaction.getAmount());
                    transactionDocument.setAmountCost(transaction.getAmountCost());
                    transactionDocument.setTypeOfTransaction(transaction.getType());

                    userDocument.getCustomer().getAccount().getTransactions().add(transactionDocument);

                    return template.save(userDocument).thenReturn(transaction);
                });
    }
}
