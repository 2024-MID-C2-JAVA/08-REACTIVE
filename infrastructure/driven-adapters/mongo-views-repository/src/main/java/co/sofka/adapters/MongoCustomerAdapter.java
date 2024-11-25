package co.sofka.adapters;

import co.sofka.Customer;
import co.sofka.data.CustomerDocument;
import co.sofka.data.UserDocument;
import co.sofka.out.CustomerViewsRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class MongoCustomerAdapter implements CustomerViewsRepository {

    private final ReactiveMongoTemplate template;


    public MongoCustomerAdapter(ReactiveMongoTemplate template) {
        this.template = template;
    }


    @Override
    public Mono<Customer> getCustomer(Customer customer) {
        return template.findById(customer.getId(), CustomerDocument.class)
                .flatMap(customerDocument -> {
                    customer.setName(customerDocument.getName());
                    customer.setCreatedAt(customerDocument.getCreatedAt());
                    return Mono.just(customer);
                });
    }

    @Override
    public Mono<Customer> getCustomerByEmail(Customer customer) {
        Query query = new Query(Criteria.where("email").is(customer.getName()));
        return template.findOne(query, UserDocument.class)
                .flatMap(userDocument -> {
                    customer.setId(userDocument.getId());
                    customer.setName(userDocument.getCustomer().getName());
                    customer.setCreatedAt(userDocument.getCustomer().getCreatedAt());
                    return Mono.just(customer);
                });
    }
}
