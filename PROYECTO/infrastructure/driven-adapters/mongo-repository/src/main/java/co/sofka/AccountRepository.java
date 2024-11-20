package co.sofka;



import co.sofka.config.JpaBanktransactionRepository;
import co.sofka.data.entity.CustomerEntity;
import co.sofka.gateway.IAccountRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class AccountRepository implements IAccountRepository {

    private static final Logger logger = LoggerFactory.getLogger(AccountRepository.class);

    private final JpaBanktransactionRepository repository;

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<Account> save(Account item) {
        Flux<CustomerEntity> banktransaction = mongoTemplate.findAll(CustomerEntity.class, "banktransaction");
        banktransaction.map(customerEntity -> {
            customerEntity.getAccounts().stream().forEach(accountEntity -> {
                if (accountEntity.getNumber().equals(item.getNumber())){
                    accountEntity.setAmount(item.getAmount());
                    mongoTemplate.save(customerEntity);
                }
            });
            return customerEntity;
        });
       return Mono.just(item);
    }

    @Override
    public  Mono<Account> findByNumber(String accountNumber) {
        CustomerEntity item = repository.findByNumberAccount(accountNumber);
        logger.info("Account found: {} {} ", item.getAccounts().size(),item.getUsername());
        if(item.getAccounts() != null){

            List<Account> collect = item.getAccounts().stream().map(accountEntity -> {
                Account account = new Account();
                account.setId(accountEntity.getId());
                account.setNumber(accountEntity.getNumber());
                account.setAmount(accountEntity.getAmount());
                logger.info("Account found: " + accountEntity.getNumber());

                return account;
            }).collect(Collectors.toList());

            return Mono.just(collect.stream().filter(account -> account.getNumber().equals(accountNumber)).findFirst().orElse(null));

        }
        return null;
    }

    @Override
    public Mono<Account> findById(String id) {

        return Mono.just(null);
    }

    @Override
    public Flux<Account> getAll() {
        return Flux.fromIterable(null);
    }



}
