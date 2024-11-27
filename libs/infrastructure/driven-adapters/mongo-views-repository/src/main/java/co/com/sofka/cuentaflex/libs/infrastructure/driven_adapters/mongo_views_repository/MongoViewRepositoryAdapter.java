package co.com.sofka.cuentaflex.libs.infrastructure.driven_adapters.mongo_views_repository;

import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.ViewRepositoryPort;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.data.AccountView;
import co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.data.CustomerView;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class MongoViewRepositoryAdapter implements ViewRepositoryPort {
    private final static String CUSTOMER_VIEW_COLLECTION = "customers";

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public MongoViewRepositoryAdapter(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Mono<Void> saveCustomerView(CustomerView customerView) {
        return this.reactiveMongoTemplate.save(customerView, CUSTOMER_VIEW_COLLECTION).then();
    }

    @Override
    public Mono<Void> saveAccountToCustomerView(String customerId, AccountView accountView) {
        Query query = Query.query(Criteria.where("customerId").is(customerId));
        Update update = new Update().push("accounts", accountView);
        return this.reactiveMongoTemplate.updateFirst(query, update, CUSTOMER_VIEW_COLLECTION).then();
    }
}
