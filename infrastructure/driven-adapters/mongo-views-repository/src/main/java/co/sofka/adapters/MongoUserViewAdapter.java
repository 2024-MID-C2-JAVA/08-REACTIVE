package co.sofka.adapters;

import co.sofka.AuthenticationRequest;
import co.sofka.UserRequest;
import co.sofka.data.UserDocument;
import co.sofka.jwt.UserViewsRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class MongoUserViewAdapter implements UserViewsRepository {

    private final ReactiveMongoTemplate template;


    public MongoUserViewAdapter(ReactiveMongoTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<UserRequest> getUserByEmail(AuthenticationRequest authenticationRequest) {
        Query query = new Query(Criteria.where("email").is(authenticationRequest.getEmail()));

        return template.findOne(query, UserDocument.class)
                .flatMap(userDocument -> {

                    if (userDocument == null) {
                        return Mono.error(new RuntimeException("User not found"));
                    }

                    return Mono.just(new UserRequest.Builder()
                            .id(userDocument.getId())
                            .firstname(userDocument.getFirstName())
                            .lastname(userDocument.getLastName())
                            .email(userDocument.getEmail())
                            .role(userDocument.getRole())
                            .build());
                });
    }
}
