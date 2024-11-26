package com.bank.management;

import com.bank.management.command.*;
import com.bank.management.customer.Account;
import com.bank.management.customer.Customer;
import com.bank.management.gateway.EventBusListener;
import com.bank.management.generic.DomainEvent;
import com.bank.management.usecase.queryservice.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitListenerImpl implements EventBusListener {

    private final UpdateViewUserAddedUseCase updateViewUserAddedUseCase;
    private final UpdateViewAccountAddedUseCase updateViewAccountAddedUseCase;
    private final UpdateViewAccountDeletedUseCase updateViewAccountDeletedUseCase;
    private final UpdateViewCustomerAddedUseCase updateViewCustomerAddedUseCase;
    private final UpdateViewCustomerDeletedUseCase updateViewCustomerDeletedUseCase;
    private final UpdateViewDepositAddedUseCase updateViewDepositAddedUseCase;
    private final UpdateViewPurchaseAddedWithCardUseCase processPurchaseCommand;
    private final UpdateViewWithdrawAddedUseCase processWithdrawalCommand;
    private final ObjectMapper objectMapper;

    public RabbitListenerImpl(UpdateViewUserAddedUseCase updateViewUserAddedUseCase, UpdateViewAccountAddedUseCase updateViewAccountAddedUseCase, UpdateViewAccountDeletedUseCase updateViewAccountDeletedUseCase, UpdateViewCustomerAddedUseCase updateViewCustomerAddedUseCase, UpdateViewCustomerDeletedUseCase updateViewCustomerDeletedUseCase, UpdateViewDepositAddedUseCase updateViewDepositAddedUseCase, UpdateViewPurchaseAddedWithCardUseCase processPurchaseCommand, UpdateViewWithdrawAddedUseCase processWithdrawalCommand, ObjectMapper objectMapper) {
        this.updateViewUserAddedUseCase = updateViewUserAddedUseCase;
        this.updateViewAccountAddedUseCase = updateViewAccountAddedUseCase;
        this.updateViewAccountDeletedUseCase = updateViewAccountDeletedUseCase;
        this.updateViewCustomerAddedUseCase = updateViewCustomerAddedUseCase;
        this.updateViewCustomerDeletedUseCase = updateViewCustomerDeletedUseCase;
        this.updateViewDepositAddedUseCase = updateViewDepositAddedUseCase;
        this.processPurchaseCommand = processPurchaseCommand;
        this.processWithdrawalCommand = processWithdrawalCommand;
        this.objectMapper = objectMapper;
    }

    @Override
    @RabbitListener(queues = "#{@queueNameCreateUser}")
    public void createUserEvent(DomainEvent event) {
        try {
            CreateUserCommand command = objectMapper.readValue(event.body(), CreateUserCommand.class);

            updateViewUserAddedUseCase.apply(command).subscribe();

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid event format", e);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @Override
    @RabbitListener(queues = "#{@queueNameCreateAccount}")
    public void createBankAccountEvent(DomainEvent event) {
        try {
            CreateAccountCommand command = objectMapper.readValue(event.body(), CreateAccountCommand.class);
            Customer customer = new Customer.Builder().id(event.aggregateRootId()).build();
            Account account = new Account.Builder().amount(command.getAmount()).build();

            updateViewAccountAddedUseCase.apply(account, customer).subscribe();

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid event format", e);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @Override
    @RabbitListener(queues = "#{@queueNameDeleteAccount}")
    public void deleteBankAccountEvent(DomainEvent event) {
        try {
            DeleteBankAccountCommand command = objectMapper.readValue(event.body(),DeleteBankAccountCommand.class);
            updateViewAccountDeletedUseCase.apply(command.getAggregateRootId()).subscribe();

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid event format", e);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @Override
    @RabbitListener(queues = "#{@queueNameDeleteCustomer}")
    public void deleteCustomerEvent(DomainEvent event) {
        try {
            DeleteCustomerCommand command = objectMapper.readValue(event.body(), DeleteCustomerCommand.class);
            updateViewCustomerDeletedUseCase.apply(command.getAggregateRootId()).subscribe();

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid event format", e);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @Override
    @RabbitListener(queues = "#{@queueNameCreateCustomer}")
    public void createCustomerEvent(DomainEvent event) {
        try {
            CreateCustomerCommand command = objectMapper.readValue(event.body(), CreateCustomerCommand.class);

            Customer customer = new Customer.Builder()
                    .username(command.getUsername())
                            .lastname(command.getLastname())
                                    .name(command.getName())
                                            .build();

            updateViewCustomerAddedUseCase.apply(customer).subscribe();

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid event format", e);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @Override
    @RabbitListener(queues = "#{@queueNameDeposit}")
    public void deposit(DomainEvent event) {
        try {
            ProcessDepositCommand command = objectMapper.readValue(event.body(), ProcessDepositCommand.class);
            updateViewDepositAddedUseCase.processDeposit(command).subscribe();

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid event format", e);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @Override
    @RabbitListener(queues = "#{@queueNamePurchase}")
    public void purchase(DomainEvent event) {
        try {
            ProcessPurchaseCommand command = objectMapper.readValue(event.body(), ProcessPurchaseCommand.class);
            processPurchaseCommand.apply(command).subscribe();

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid event format", e);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @Override
    @RabbitListener(queues = "#{@queueNameWithdraw}")
    public void withdrawal(DomainEvent event) {
        try {
            ProcessWithdrawalCommand command = objectMapper.readValue(event.body(), ProcessWithdrawalCommand.class);
            processWithdrawalCommand.apply(command).subscribe();

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid event format", e);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

}
