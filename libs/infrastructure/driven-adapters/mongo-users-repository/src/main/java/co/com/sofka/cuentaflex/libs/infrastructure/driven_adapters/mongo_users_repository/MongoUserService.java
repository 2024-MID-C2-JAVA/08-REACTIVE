package co.com.sofka.cuentaflex.libs.infrastructure.driven_adapters.mongo_users_repository;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MongoUserService implements ReactiveUserDetailsService {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public MongoUserService(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        Query query = Query.query(Criteria.where("username").is(username));
        return reactiveMongoTemplate.findOne(query, UserDocument.class)
                .map(user -> user);
    }
}
