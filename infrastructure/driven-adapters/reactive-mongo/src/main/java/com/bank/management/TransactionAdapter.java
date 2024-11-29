package com.bank.management;

import com.bank.management.values.Account;
import com.bank.management.values.Customer;
import com.bank.management.data.CustomerDocument;
import com.bank.management.data.TransactionDocument;
import com.bank.management.exception.AccountNotBelongsToCustomerException;
import com.bank.management.exception.CustomerNotFoundException;
import com.bank.management.gateway.TransactionRepository;
import com.bank.management.mapper.TransactionMapper;
import com.bank.management.values.Transaction;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Date;

@Component
public class TransactionAdapter implements TransactionRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public TransactionAdapter(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    @Transactional
    public Mono<Transaction> save(Transaction trx, Account account, Customer customer, String role) {
        TransactionDocument transactionDocument = TransactionMapper.toDocument(trx);
        if (transactionDocument.getId() == null) {
            transactionDocument.setId(new ObjectId().toString());
        }
        if (transactionDocument.getTimeStamp() == null) {
            transactionDocument.setTimeStamp(new Date());
        }

        Query customerQuery = new Query(Criteria.where("_id").is(customer.getId())
                .and("accounts._id").is(account.getId()));

        return reactiveMongoTemplate.findOne(customerQuery, CustomerDocument.class)
                .flatMap(customerDocument -> {

                    Update updateTransactions = new Update().push("accounts.$.transactions", transactionDocument);
                    Update updateAccountBalance = new Update().set("accounts.$.amount", account.getAmount());

                    Mono<UpdateResult> updateTransactionsMono = reactiveMongoTemplate.updateFirst(customerQuery, updateTransactions, CustomerDocument.class);
                    Mono<UpdateResult> updateAccountBalanceMono = reactiveMongoTemplate.updateFirst(customerQuery, updateAccountBalance, CustomerDocument.class);


                    return Mono.zip(updateTransactionsMono, updateAccountBalanceMono)
                            .flatMap(results -> {
                                UpdateResult transactionsResult = results.getT1();
                                UpdateResult balanceResult = results.getT2();

                                if (transactionsResult.getModifiedCount() > 0 && balanceResult.getModifiedCount() > 0) {
                                    return Mono.just(TransactionMapper.toDomain(transactionDocument));
                                } else {
                                    return Mono.error(new CustomerNotFoundException(customer.getId().value()));
                                }
                            });
                })
                .switchIfEmpty(Mono.error(new AccountNotBelongsToCustomerException()));
    }
}
