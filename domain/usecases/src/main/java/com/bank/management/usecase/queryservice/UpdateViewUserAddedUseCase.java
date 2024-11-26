package com.bank.management.usecase.queryservice;

import com.bank.management.command.CreateUserCommand;
import com.bank.management.customer.Customer;
import com.bank.management.customer.User;
import com.bank.management.gateway.UserRepository;
import reactor.core.publisher.Mono;

public class UpdateViewUserAddedUseCase {

    private final UserRepository userRepository;


    public UpdateViewUserAddedUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> apply(CreateUserCommand command) {
        User user = new User(command.getUsername(), command.getPassword(), command.getRoles());
        Customer customer = new Customer.Builder()
                .username(command.getUsername())
                .lastname(command.getLastname())
                .name(command.getName())
                .build();
        return userRepository.saveUserAndCustomer(user, customer);
    }
}
