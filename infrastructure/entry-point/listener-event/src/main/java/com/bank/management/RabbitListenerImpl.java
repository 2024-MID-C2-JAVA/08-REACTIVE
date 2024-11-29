package com.bank.management;

import com.bank.management.command.*;
import com.bank.management.events.AccountCreated;
import com.bank.management.events.CustomerCreated;
import com.bank.management.values.Account;
import com.bank.management.values.Customer;
import com.bank.management.gateway.EventBusListener;
import com.bank.management.generic.DomainEvent;
import com.bank.management.usecase.queryservice.*;
import com.bank.management.values.account.Amount;
import com.bank.management.values.account.Number;
import com.bank.management.values.customer.CustomerId;
import com.bank.management.values.customer.Lastname;
import com.bank.management.values.customer.Name;
import com.bank.management.values.customer.Username;
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

    public RabbitListenerImpl(UpdateViewUserAddedUseCase updateViewUserAddedUseCase, UpdateViewAccountAddedUseCase updateViewAccountAddedUseCase, UpdateViewAccountDeletedUseCase updateViewAccountDeletedUseCase, UpdateViewCustomerAddedUseCase updateViewCustomerAddedUseCase, UpdateViewCustomerDeletedUseCase updateViewCustomerDeletedUseCase, UpdateViewDepositAddedUseCase updateViewDepositAddedUseCase, UpdateViewPurchaseAddedWithCardUseCase processPurchaseCommand, UpdateViewWithdrawAddedUseCase processWithdrawalCommand) {
        this.updateViewUserAddedUseCase = updateViewUserAddedUseCase;
        this.updateViewAccountAddedUseCase = updateViewAccountAddedUseCase;
        this.updateViewAccountDeletedUseCase = updateViewAccountDeletedUseCase;
        this.updateViewCustomerAddedUseCase = updateViewCustomerAddedUseCase;
        this.updateViewCustomerDeletedUseCase = updateViewCustomerDeletedUseCase;
        this.updateViewDepositAddedUseCase = updateViewDepositAddedUseCase;
        this.processPurchaseCommand = processPurchaseCommand;
        this.processWithdrawalCommand = processWithdrawalCommand;
    }

    @Override
    @RabbitListener(queues = "#{@queueNameCreateUser}")
    public void createUserEvent(DomainEvent event) {
        try {
            CreateUserCommand command = new CreateUserCommand();

            updateViewUserAddedUseCase.apply(command).subscribe();

        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @Override
    @RabbitListener(queues = "#{@queueNameCreateAccount}")
    public void createBankAccountEvent(AccountCreated event) {
        try {
            Customer customer = new Customer.Builder().id(CustomerId.of(event.aggregateRootId())).build();
            Account account = new Account.Builder()
                    .amount(Amount.of(event.getAmount()))
                            .number(Number.of(event.getNumber()))
                                    .build();

            updateViewAccountAddedUseCase.apply(account, customer).subscribe();

        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @Override
    @RabbitListener(queues = "#{@queueNameDeleteAccount}")
    public void deleteBankAccountEvent(DomainEvent event) {
        try {
            DeleteBankAccountCommand command = new DeleteBankAccountCommand();
            updateViewAccountDeletedUseCase.apply(command.getAggregateRootId()).subscribe();

        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @Override
    @RabbitListener(queues = "#{@queueNameDeleteCustomer}")
    public void deleteCustomerEvent(DomainEvent event) {
        try {
            DeleteCustomerCommand command = new DeleteCustomerCommand();
            updateViewCustomerDeletedUseCase.apply(command.getAggregateRootId()).subscribe();

        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @Override
    @RabbitListener(queues = "#{@queueNameCreateCustomer}")
    public void createCustomerEvent(CustomerCreated event) {
        try {

            Customer customer = new Customer.Builder()
                    .username(Username.of(event.getUsername()))
                    .lastname(Lastname.of(event.getLastname()))
                    .name(Name.of(event.getName()))
                    .id(CustomerId.of(event.getId()))
                    .build();

            updateViewCustomerAddedUseCase.apply(customer).subscribe();

        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @Override
    @RabbitListener(queues = "#{@queueNameDeposit}")
    public void deposit(DomainEvent event) {
        try {
            ProcessDepositCommand command = new ProcessDepositCommand();
            updateViewDepositAddedUseCase.processDeposit(command).subscribe();

        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @Override
    @RabbitListener(queues = "#{@queueNamePurchase}")
    public void purchase(DomainEvent event) {
        try {
            ProcessPurchaseCommand command = new ProcessPurchaseCommand();
            processPurchaseCommand.apply(command).subscribe();

        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    @Override
    @RabbitListener(queues = "#{@queueNameWithdraw}")
    public void withdrawal(DomainEvent event) {
        try {
            ProcessWithdrawalCommand command = new ProcessWithdrawalCommand();
            processWithdrawalCommand.apply(command).subscribe();

        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

}
