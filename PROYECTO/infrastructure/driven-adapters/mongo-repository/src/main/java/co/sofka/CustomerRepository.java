package co.sofka;



import co.sofka.config.JpaBanktransactionRepository;
import co.sofka.data.entity.CustomerEntity;
import co.sofka.gateway.ICustomerRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class CustomerRepository implements ICustomerRepository {

    private static final Logger logger = LoggerFactory.getLogger(CustomerRepository.class);

    //private final JpaBanktransactionRepository repository;

    private final ReactiveMongoTemplate mongoTemplate;


    @Override
    public Mono<Customer> save(Customer customer) {
        if (customer.getAccounts()==null){
            customer.setAccounts(new ArrayList<>());
        }
        mongoTemplate.save(customer, "banktransaction");
        return Mono.just(customer);
    }

    @Override
    public Mono<Customer> findById(String id) {
        return null;
    }

    @Override
    public Mono<Customer> findByUsername(String username) {
        Flux<CustomerEntity> all = mongoTemplate.findAll(CustomerEntity.class, "banktransaction");

        Mono<List<CustomerEntity>> listMono = all.filter(item -> item.getUsername().equals(username)).collectList();

//        all = all.map().filter(item->item.getUsername().equals(username)).collect(Collectors.toList());

        if(listMono.block().size() == 0){
            return null;
        }
        CustomerEntity item = listMono.block().get(0);
        Customer customer = new Customer();
        customer.setId(item.getId().toString());
        customer.setUsername(item.getUsername());
        customer.setPwd(item.getPwd());
        customer.setRol(item.getRol());
        customer.setAccounts(new ArrayList<>());
        if(item.getAccounts() != null){
            customer.setAccounts(item.getAccounts().stream().map(accountEntity -> {
                Account account = new Account();
                account.setId(accountEntity.getId());
                account.setNumber(accountEntity.getNumber());
                account.setAmount(accountEntity.getAmount());
                account.setCustomer(customer);
                return account;
            }).collect(Collectors.toList()));
        }
        return Mono.just(customer);

    }

    @Override
    public Flux<Customer> getAll() {
//        List<CustomerEntity> all = repository.findAll();
        Flux<CustomerEntity> all = mongoTemplate.findAll(CustomerEntity.class, "banktransaction");

        Flux<Customer> customers = all.map(item->{
            Customer customer = new Customer();
            customer.setId(item.getId().toString());
            customer.setUsername(item.getUsername());
            customer.setPwd(item.getPwd());
            customer.setRol(item.getRol());
            customer.setAccounts(new ArrayList<>());
            if(item.getAccounts() != null){

                item.getAccounts().stream().forEach(accountEntity -> {
                    Account account = new Account();
                    account.setId(accountEntity.getId());
                    account.setNumber(accountEntity.getNumber());
                    account.setAmount(accountEntity.getAmount());
                    account.setCustomer(customer);
                    customer.getAccounts().add(account);
                });

//                customer.setAccounts(item.getAccounts().collectList().);
//
//                Mono<Customer> map = item.getAccounts().collectList().map(accountEntities -> {
//                    accountEntities.stream().forEach(accountEntity -> {
//                        Account account = new Account();
//                        account.setId(accountEntity.getId());
//                        account.setNumber(accountEntity.getNumber());
//                        account.setAmount(accountEntity.getAmount());
//                        account.setCustomer(customer);
//                        customer.getAccounts().add(account);
//                    });
//                    return customer;
//                });
//                customer.setAccounts(item.getAccounts().map(accountEntity -> {
//                    Account account = new Account();
//                    account.setId(accountEntity.getId());
//                    account.setNumber(accountEntity.getNumber());
//                    account.setAmount(accountEntity.getAmount());
//                    account.setCustomer(customer);
//                    return account;
//                }).);
            }
        return customer;
        });

//        customers = all.map(item->{
//            Customer customer = new Customer();
//            customer.setId(item.getId().toString());
//            customer.setUsername(item.getUsername());
//            customer.setPwd(item.getPwd());
//            customer.setRol(item.getRol());
//            customer.setAccounts(new ArrayList<>());
//            if(item.getAccounts() != null){
//                customer.setAccounts(item.getAccounts().map(accountEntity -> {
//                    Account account = new Account();
//                    account.setId(accountEntity.getId());
//                    account.setNumber(accountEntity.getNumber());
//                    account.setAmount(accountEntity.getAmount());
//                    account.setCustomer(customer);
//                    return account;
//                }));
//            }
//
//
//
//            return customer;
//        }).collect(Collectors.toList());

        return customers;
    }




}
