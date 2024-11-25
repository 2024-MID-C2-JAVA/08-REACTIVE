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

    private final CreateUserUseCase createUserUseCase;
    private final CreateBankAccountUseCase createBankAccountUseCase;
    private final ProcessDepositUseCase processDepositUseCase;
    private final ProcessPurchaseWithCardUseCase processPurchaseCommand;
    private final ProcessWithdrawUseCase processWithdrawalCommand;
    private final ObjectMapper objectMapper;

    public RabbitListenerImpl(CreateUserUseCase createUserUseCase, CreateBankAccountUseCase createBankAccountUseCase, ProcessDepositUseCase processDepositUseCase, ProcessPurchaseWithCardUseCase processPurchaseCommand, ProcessWithdrawUseCase processWithdrawalCommand, ObjectMapper objectMapper) {
        this.createUserUseCase = createUserUseCase;
        this.createBankAccountUseCase = createBankAccountUseCase;
        this.processDepositUseCase = processDepositUseCase;
        this.processPurchaseCommand = processPurchaseCommand;
        this.processWithdrawalCommand = processWithdrawalCommand;
        this.objectMapper = objectMapper;
    }

    @Override
    @RabbitListener(queues = "#{@queueNameCreateUser}")
    public void createUserEvent(DomainEvent event) {
        try {
            CreateUserCommand command = objectMapper.readValue(event.body(), CreateUserCommand.class);

            createUserUseCase.apply(command).subscribe();

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

            createBankAccountUseCase.apply(account, customer).subscribe();

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
            processDepositUseCase.processDeposit(command).subscribe();

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
