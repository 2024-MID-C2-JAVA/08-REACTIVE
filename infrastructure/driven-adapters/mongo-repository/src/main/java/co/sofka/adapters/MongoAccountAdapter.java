package co.sofka.adapters;

import co.sofka.Account;
import co.sofka.data.AccountDocument;
import co.sofka.data.UserDocument;
import co.sofka.out.AccountRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public class MongoAccountAdapter implements AccountRepository {

    private final ReactiveMongoTemplate template;

    public MongoAccountAdapter(ReactiveMongoTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Account> createAccount(Account account) {
        return template.findById(account.getCustomerId(), UserDocument.class)
                .flatMap(userDocument -> {
                    AccountDocument accountDocument = new AccountDocument();
                    accountDocument.setNumber(account.getNumber());
                    accountDocument.setAmount(account.getAmount());
                    accountDocument.setCreatedAt(LocalDate.now());
                    accountDocument.setDeleted(false);
                    accountDocument.setCustomerId(userDocument.getCustomer().getId());

                    userDocument.getCustomer().setAccount(accountDocument);

                    return template.save(userDocument)
                            .thenReturn(account);
                }).switchIfEmpty(Mono.error(new RuntimeException("Account not found with ID: " + account.getCustomerId())));
    }

    @Override
    public Mono<Account> deleteAccount(Account account) {
        return template.findById(account.getId(), UserDocument.class)
                .flatMap(document -> {
                    document.getCustomer().getAccount().setDeleted(true);
                    return template
                            .save(document)
                            .thenReturn(account);
                }).switchIfEmpty(Mono.error(new RuntimeException("Account not found with ID: " + account.getCustomerId())));
    }

    @Override
    public Mono<Account> updateAccount(Account account) {
        return template.findById(account.getId(),UserDocument.class)
                .flatMap(userDocument -> {
                    BigDecimal amount=account.getAmount();
                    userDocument.getCustomer().getAccount().setAmount(amount);
                    return template.save(userDocument)
                            .thenReturn(account);
                }).switchIfEmpty(Mono.error(new RuntimeException("Account not found with ID: " + account.getCustomerId())));
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
