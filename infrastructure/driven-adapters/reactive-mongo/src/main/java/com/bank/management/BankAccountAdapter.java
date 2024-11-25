package com.bank.management;

import com.bank.management.customer.Account;
import com.bank.management.customer.Customer;
import com.bank.management.data.AccountDocument;
import com.bank.management.data.CustomerDocument;
import com.bank.management.exception.BankAccountNotFoundException;
import com.bank.management.gateway.AccountRepository;
import com.bank.management.mapper.AccountMapper;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.security.auth.login.AccountNotFoundException;

@Component
public class BankAccountAdapter implements AccountRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public BankAccountAdapter(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;

    }

    @Override
    public Mono<Account> findById(String id) {
        Query query = new Query(Criteria.where("accounts._id").is(id));
        query.fields().include("accounts.$");

        return reactiveMongoTemplate.findOne(query, CustomerDocument.class)
                .flatMap(customerDocument -> {
                    if (customerDocument.getAccounts() == null || customerDocument.getAccounts().isEmpty()) {
                        return Mono.empty();
                    }
                    AccountDocument accountDocument = customerDocument.getAccounts().get(0);
                    return Mono.just(AccountMapper.toDomain(accountDocument));
                });
    }
    @Override
    public Mono<Boolean> delete(String id) {
        Query query = new Query(Criteria.where("accounts._id").is(id));
        Update update = new Update().set("accounts.$.isDeleted", true);

        return reactiveMongoTemplate
                .updateFirst(query, update, CustomerDocument.class)
                .flatMap(result -> {
                    if (result.getModifiedCount() > 0) {
                        return Mono.just(true);
                    } else {
                        return Mono.error(new BankAccountNotFoundException());
                    }
                });
    }

    @Override
    public Flux<Account> findByCustomerId(String id) {
        Query query = new Query(Criteria.where("_id").is(id));

        return reactiveMongoTemplate.findOne(query, CustomerDocument.class)
                .flatMapMany(customerDocument -> {
                    if (customerDocument.getAccounts() == null || customerDocument.getAccounts().isEmpty()) {
                        return Flux.empty();
                    }
                    return Flux.fromStream(customerDocument.getAccounts().stream())
                            .map(AccountMapper::toDomain);
                });
    }

    @Override
    public Mono<Account> findByNumber(String accountNumber) {
        Query query = new Query(Criteria.where("accounts.number").is(accountNumber));

        return reactiveMongoTemplate.findOne(query, CustomerDocument.class)
                .flatMap(customerDocument -> {
                    return Mono.justOrEmpty(
                            customerDocument.getAccounts().stream()
                                    .filter(account -> account.getNumber().equals(accountNumber))
                                    .findFirst()
                                    .map(AccountMapper::toDomain)
                    );
                })
                .switchIfEmpty(Mono.error(new AccountNotFoundException()));
    }


    @Override
    public Mono<Account> save(Account account) {
        AccountDocument accountDocument = AccountMapper.toDocument(account);

        if (accountDocument.getId() == null) {
            accountDocument.setId(new ObjectId().toString());
        }

        if (account.getCustomer() == null) {
            return Mono.error(new IllegalArgumentException("El cliente no puede ser nulo."));
        }

        Customer customer = account.getCustomer();

        Query query = new Query(Criteria.where("_id").is(customer.getId())
                .and("accounts._id").is(accountDocument.getId()));

        return reactiveMongoTemplate.exists(query, CustomerDocument.class)
                .flatMap(exists -> {
                    Query customerQuery = new Query(Criteria.where("_id").is(customer.getId()));
                    Update update;

                    if (exists) {
                        update = new Update()
                                .set("accounts.$.number", accountDocument.getNumber())
                                .set("accounts.$.amount", accountDocument.getAmount())
                                .set("accounts.$.customerId", accountDocument.getCustomerId())
                                .set("accounts.$.isDeleted", accountDocument.isDeleted());
                    } else {
                        update = new Update().push("accounts", accountDocument);
                    }

                    return reactiveMongoTemplate.updateFirst(customerQuery, update, CustomerDocument.class)
                            .flatMap(updateResult -> {
                                if (updateResult.getModifiedCount() > 0) {
                                    return Mono.just(AccountMapper.toDomain(accountDocument));
                                } else {
                                    return Mono.error(new AccountNotFoundException());
                                }
                            });
                });
    }

}
