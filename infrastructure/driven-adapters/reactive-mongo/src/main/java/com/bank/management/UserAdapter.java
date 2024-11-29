package com.bank.management;

import com.bank.management.values.Customer;
import com.bank.management.values.User;
import com.bank.management.data.CustomerDocument;
import com.bank.management.data.UserDocument;
import com.bank.management.exception.UserAlreadyExistsException;
import com.bank.management.gateway.UserRepository;
import com.bank.management.mapper.CustomerMapper;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
public class UserAdapter implements UserRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final PasswordEncoder passwordEncoder;

    public UserAdapter(ReactiveMongoTemplate reactiveMongoTemplate, PasswordEncoder passwordEncoder) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public Mono<User> saveUserAndCustomer(User user, Customer customer) {
        Query userQuery = new Query(Criteria.where("username").is(user.getUsername()));

        return reactiveMongoTemplate.findOne(userQuery, CustomerDocument.class, "customer")
                .switchIfEmpty(
                        reactiveMongoTemplate.save(CustomerMapper.toDocument(customer), "customer")
                                .then(reactiveMongoTemplate.findOne(userQuery, CustomerDocument.class, "customer"))
                )
                .flatMap(customerDocument -> {

                    if (customerDocument.getUser() != null) {
                        return Mono.error(new UserAlreadyExistsException(user.getUsername()));
                    }

                    customerDocument.setUser(new UserDocument(
                            user.getUsername(),
                            passwordEncoder.encode(user.getPassword()),
                            user.getRoles()
                    ));

                    return reactiveMongoTemplate.save(customerDocument, "customer");
                })
                .map(updatedCustomerDocument -> {
                    UserDocument userDocument = updatedCustomerDocument.getUser();
                    return new User(
                            userDocument.getUsername(),
                            userDocument.getPassword(),
                            userDocument.getRoles()
                    );
                });
    }


    @Override
    public Mono<User> findByUsername(String username) {
        Query query = new Query(Criteria.where("username").is(username));

        return reactiveMongoTemplate.findOne(query, CustomerDocument.class, "customer")
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found for username: " + username)))
                .flatMap(customerDocument -> {
                    if (customerDocument.getUser() == null) {
                        return Mono.error(new UsernameNotFoundException("No user data for username: " + username));
                    }

                    UserDocument userDocument = customerDocument.getUser();
                    return Mono.just(new User(
                            userDocument.getUsername(),
                            userDocument.getPassword(),
                            userDocument.getRoles()
                    ));
                });
    }
}
