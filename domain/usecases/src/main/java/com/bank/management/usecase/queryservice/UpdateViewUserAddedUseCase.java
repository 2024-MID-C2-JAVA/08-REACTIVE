package com.bank.management.usecase.queryservice;

import com.bank.management.command.CreateUserCommand;
import com.bank.management.values.Customer;
import com.bank.management.values.User;
import com.bank.management.gateway.UserRepository;
import com.bank.management.values.customer.Lastname;
import com.bank.management.values.customer.Name;
import com.bank.management.values.customer.Username;
import reactor.core.publisher.Mono;

public class UpdateViewUserAddedUseCase {

    private final UserRepository userRepository;


    public UpdateViewUserAddedUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> apply(CreateUserCommand command) {
        User user = new User(command.getUsername(), command.getPassword(), command.getRoles());
        Customer customer = new Customer.Builder()
                .username(Username.of(command.getUsername()))
                .lastname(Lastname.of(command.getLastname()))
                .name(Name.of(command.getName()))
                .build();
        return userRepository.saveUserAndCustomer(user, customer);
    }
}
