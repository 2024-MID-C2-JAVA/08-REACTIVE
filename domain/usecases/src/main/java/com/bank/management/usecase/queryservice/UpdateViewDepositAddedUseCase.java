package com.bank.management.usecase.queryservice;

import com.bank.management.command.ProcessDepositCommand;
import com.bank.management.values.Account;
import com.bank.management.values.Customer;
import com.bank.management.enums.DepositType;
import com.bank.management.exception.BankAccountNotFoundException;
import com.bank.management.exception.CustomerNotFoundException;
import com.bank.management.exception.InvalidAmountException;
import com.bank.management.exception.InvalidDepositTypeException;
import com.bank.management.gateway.AccountRepository;
import com.bank.management.gateway.CustomerRepository;
import com.bank.management.gateway.TransactionRepository;
import com.bank.management.values.Transaction;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class UpdateViewDepositAddedUseCase {

    private final AccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    public UpdateViewDepositAddedUseCase(AccountRepository bankAccountRepository, CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    public Mono<Account> processDeposit(ProcessDepositCommand command) {
        return validateDepositAmount(command)
                .then(bankAccountRepository.findByNumber(command.getAccountNumber())
                        .switchIfEmpty(Mono.error(new BankAccountNotFoundException())))
                .zipWith(customerRepository.findById(command.getAggregateRootId())
                        .switchIfEmpty(Mono.error(new CustomerNotFoundException(command.getAggregateRootId()))))
                .flatMap(tuple -> {
                    Account account = tuple.getT1();
                    Customer customer = tuple.getT2();

                    DepositType depositType = parseDepositTypeReactive(command.getType());
                    BigDecimal fee = calculateDepositFee(depositType);

                    account.adjustBalance(command.getAmount().subtract(fee));

                    Transaction trx = new Transaction.Builder()
                            .amountTransaction(command.getAmount())
                            .transactionCost(fee)
                            .typeTransaction(depositType.toString())
                            .build();

                    return transactionRepository.save(trx, account, customer, "RECEIVED")
                            .then(Mono.just(account));
                });
    }

    private Mono<Void> validateDepositAmount(ProcessDepositCommand command) {
        if (command.getAmount() == null || command.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.error(new InvalidAmountException());
        }
        return Mono.empty();
    }

    private DepositType parseDepositTypeReactive(String type) {
        try {
            return DepositType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidDepositTypeException(type);
        }
    }


    private BigDecimal calculateDepositFee(DepositType type) {
        return switch (type) {
            case ATM -> new BigDecimal("2.00"); // 2 USD
            case OTHER_ACCOUNT -> new BigDecimal("1.50"); // 1.5 USD
            case BRANCH -> BigDecimal.ZERO;
            default -> throw new InvalidDepositTypeException(type.toString().toUpperCase());
        };
    }
}
