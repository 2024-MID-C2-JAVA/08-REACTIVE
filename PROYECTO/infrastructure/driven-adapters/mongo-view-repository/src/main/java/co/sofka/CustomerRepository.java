package co.sofka;



import co.sofka.config.JpaBanktransactionRepository;
import co.sofka.data.entity.AccountEntity;
import co.sofka.data.entity.CustomerEntity;
import co.sofka.data.entity.TransactionAccountDetailEntity;
import co.sofka.data.entity.TransactionEntity;
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

    private final ReactiveMongoTemplate mongoTemplate;


    @Override
    public Mono<Customer> save(Customer customer) {

        logger.info("Save customer repository");

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(customer.getId());
        customerEntity.setUsername(customer.getUsername());
        customerEntity.setPwd(customer.getPwd());
        customerEntity.setRol(customer.getRol());
        if(customer.getAccounts() != null){
            customerEntity.setAccounts(customer.getAccounts().stream().map(account -> {
                AccountEntity accountEntity = new AccountEntity();
                accountEntity.setId(account.getId());
                accountEntity.setNumber(account.getNumber());
                accountEntity.setAmount(account.getAmount());

               if (account.getTransactionAccountDetails()!=null) {
                   accountEntity.setTransactionAccountDetailEntity(account.getTransactionAccountDetails().stream().map(transactionAccountDetail -> {
                       TransactionAccountDetailEntity transactionAccountDetailEntity = new TransactionAccountDetailEntity();
                       transactionAccountDetailEntity.setId(transactionAccountDetail.getId());
                       transactionAccountDetailEntity.setTransactionRole(transactionAccountDetail.getTransactionRole());
                       TransactionEntity transactionEntity = new TransactionEntity();
                       transactionEntity.setId(transactionAccountDetail.getTransaction().getId());
                       transactionEntity.setAmountTransaction(transactionAccountDetail.getTransaction().getAmountTransaction());
                       transactionEntity.setTransactionCost(transactionAccountDetail.getTransaction().getTransactionCost());
                       transactionEntity.setTypeTransaction(transactionAccountDetail.getTransaction().getTypeTransaction());
                       transactionAccountDetailEntity.setTransaction(transactionEntity);
                       return transactionAccountDetailEntity;
                   }).collect(Collectors.toList()));
               }

                return accountEntity;
            }).collect(Collectors.toList()));
        }

        logger.info("CustomerEntity: {}", customerEntity);


        mongoTemplate.save(customerEntity)
                .doOnNext(doc -> System.out.println("Guardado: " + doc))
                .doOnError(error -> System.err.println("Error: " + error))
                .doOnSuccess(doc -> System.out.println("Success: " + doc))
                .subscribe();

        return Mono.just(customer);

    }

    @Override
    public Mono<Customer> findById(String id) {

        logger.info("CustomerRepository: findById Repository");

        return mongoTemplate.findById(id, CustomerEntity.class)
                .map(item -> {
                    logger.info("CustomerEntity: {}", item);
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
                            return account;
                        }).collect(Collectors.toList()));
                    }
                    return customer;
                });
    }

    @Override
    public Mono<Customer> findByUsername(String username) {
        Flux<CustomerEntity> all = mongoTemplate.findAll(CustomerEntity.class, "banktransaction");

        Mono<List<CustomerEntity>> listMono = all.filter(item -> item.getUsername().equals(username)).collectList();

//        all = all.map().filter(item->item.getUsername().equals(username)).collect(Collectors.toList());

        Flux<Customer> map = listMono.flatMapIterable(item -> item)
                .map(item -> {
                    logger.info("Item: {}", item);

                    Customer customer = new Customer();
                    customer.setId(item.getId().toString());
                    customer.setUsername(item.getUsername());
                    customer.setPwd(item.getPwd());
                    customer.setRol(item.getRol());
                    customer.setAccounts(new ArrayList<>());
                    if (item.getAccounts() != null) {
                        customer.setAccounts(item.getAccounts().stream().map(accountEntity -> {
                            Account account = new Account();
                            account.setId(accountEntity.getId());
                            account.setNumber(accountEntity.getNumber());
                            account.setAmount(accountEntity.getAmount());
                            account.setCustomer(customer);
                            return account;
                        }).collect(Collectors.toList()));
                    }

                    return customer;
                }).map(item -> {
                    logger.info("Item: {}", item);
                    return item;
                });

//        if(listMono.block().size() == 0){
//            return null;
//        }
//        CustomerEntity item = listMono.block().get(0);
//        Customer customer = new Customer();
//        customer.setId(item.getId().toString());
//        customer.setUsername(item.getUsername());
//        customer.setPwd(item.getPwd());
//        customer.setRol(item.getRol());
//        customer.setAccounts(new ArrayList<>());
//        if(item.getAccounts() != null){
//            customer.setAccounts(item.getAccounts().stream().map(accountEntity -> {
//                Account account = new Account();
//                account.setId(accountEntity.getId());
//                account.setNumber(accountEntity.getNumber());
//                account.setAmount(accountEntity.getAmount());
//                account.setCustomer(customer);
//                return account;
//            }).collect(Collectors.toList()));
//        }
//        logger.info("Customer: {}", customer);
//        return Mono.just(customer);

        return map.next();
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
