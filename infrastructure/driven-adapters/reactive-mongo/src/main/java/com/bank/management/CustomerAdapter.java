package com.bank.management;

import com.bank.management.values.Customer;
import com.bank.management.data.CustomerDocument;
import com.bank.management.exception.CustomerNotFoundException;
import com.bank.management.gateway.CustomerRepository;
import com.bank.management.mapper.CustomerMapper;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CustomerAdapter implements CustomerRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public CustomerAdapter(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Flux<Customer> findAll() {
        return reactiveMongoTemplate.findAll(CustomerDocument.class, "customer")
                .map(CustomerMapper::toDomain);
    }

    @Override
    public Mono<Customer> findById(String id) {
        return reactiveMongoTemplate.findById(id, CustomerDocument.class, "customer")
                .map(CustomerMapper::toDomain)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException(id)));
    }

    @Override
    public Mono<Customer> save(Customer customer) {
        CustomerDocument customerDoc = CustomerMapper.toDocument(customer);
        return reactiveMongoTemplate.save(customerDoc, "customer")
                .map(CustomerMapper::toDomain);
    }

    @Override
    public Mono<Customer> findByUsername(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        return reactiveMongoTemplate.findOne(query, CustomerDocument.class, "customer")
                .map(CustomerMapper::toDomain)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException(username)));
    }

    @Override
    public Mono<Boolean> delete(Customer customer) {
        Query query = new Query(Criteria.where("id").is(customer.getId()));
        Update update = new Update().set("isDeleted", true);

        return reactiveMongoTemplate.updateFirst(query, update, CustomerDocument.class)
                .flatMap(result -> {
                    if (result.getModifiedCount() > 0) {
                        return Mono.just(true);
                    } else {
                        return Mono.error(new CustomerNotFoundException(customer.getId().value()));
                    }
                });
    }

    @Override
    public Mono<Customer> findByNumber(String accountNumber) {
        Query query = new Query(Criteria.where("accounts.number").is(accountNumber));
        return reactiveMongoTemplate.findOne(query, CustomerDocument.class, "customer")
                .map(CustomerMapper::toDomain)
                .switchIfEmpty(Mono.error(new RuntimeException("No customer found with account number: " + accountNumber)));
    }
}
