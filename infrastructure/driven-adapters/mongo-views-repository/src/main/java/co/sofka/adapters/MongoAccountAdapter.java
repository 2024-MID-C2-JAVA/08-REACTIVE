package co.sofka.adapters;

import co.sofka.Account;
import co.sofka.data.UserDocument;
import co.sofka.out.AccountViewsRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class MongoAccountAdapter implements AccountViewsRepository {

    private final ReactiveMongoTemplate template;

    public MongoAccountAdapter(ReactiveMongoTemplate template) {
        this.template = template;
    }


    @Override
    public Mono<Account> getAccount(Account account) {
        Query query = new Query(Criteria.where("_id").is(account.getId()));

        return template.findOne(query,UserDocument.class)
                .flatMap(userDocument -> {

                    if (userDocument.getCustomer() == null || userDocument.getCustomer().getAccount() == null) {
                        return Mono.error(new RuntimeException("Account not found for the given user ID"));
                    }

                    Account userAccount1 = new Account(
                            userDocument.getCustomer().getAccount().getId(),
                            userDocument.getCustomer().getAccount().getNumber(),
                            userDocument.getCustomer().getAccount().getAmount(),
                            userDocument.getCustomer().getAccount().getCustomerId(),
                            userDocument.getCustomer().getAccount().getCreatedAt()
                    );

                    return Mono.just(userAccount1);
                }).switchIfEmpty(Mono.error(new RuntimeException("Account not found for the given ID")));
    }
}
