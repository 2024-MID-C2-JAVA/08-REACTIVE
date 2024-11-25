package com.bank.management.usecase.queryservice;

import com.bank.management.command.ProcessPurchaseCommand;
import com.bank.management.customer.Account;
import com.bank.management.customer.Customer;
import com.bank.management.transaction.Purchase;
import com.bank.management.transaction.Transaction;
import com.bank.management.enums.PurchaseType;
import com.bank.management.exception.BankAccountNotFoundException;
import com.bank.management.exception.CustomerNotFoundException;
import com.bank.management.exception.InsufficientFundsException;
import com.bank.management.exception.InvalidPurchaseTypeException;
import com.bank.management.gateway.AccountRepository;
import com.bank.management.gateway.CustomerRepository;
import com.bank.management.gateway.TransactionRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class ProcessPurchaseWithCardUseCase {

    private final AccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    public ProcessPurchaseWithCardUseCase(AccountRepository bankAccountRepository, TransactionRepository transactionRepository, CustomerRepository customerRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
    }


    public Mono<Account> apply(ProcessPurchaseCommand command) {
        return bankAccountRepository.findByNumber(command.getAccountNumber())
                .switchIfEmpty(Mono.error(new BankAccountNotFoundException()))
                .zipWith(customerRepository.findByNumber(command.getAccountNumber())
                        .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found"))))
                .flatMap(tuple -> {
                    Account account = tuple.getT1();
                    Customer customer = tuple.getT2();

                    PurchaseType purchaseType;
                    try {
                        purchaseType = PurchaseType.valueOf(command.getPurchaseType().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        return Mono.error(new InvalidPurchaseTypeException("Invalid purchase type: " + command.getPurchaseType()));
                    }

                    BigDecimal amount = command.getAmount();
                    BigDecimal fee = calculatePurchaseFee(purchaseType);
                    BigDecimal totalCharge = amount.add(fee);

                    if (account.getAmount().compareTo(totalCharge) < 0) {
                        return Mono.error(new InsufficientFundsException());
                    }

                    account.adjustBalance(totalCharge.negate());

                    Transaction trx = new Transaction.Builder()
                            .amountTransaction(amount)
                            .transactionCost(fee)
                            .typeTransaction(purchaseType.toString())
                            .build();

                    return transactionRepository.save(trx, account, customer, "BUYER")
                            .then(Mono.just(account));
                });
    }


    private BigDecimal calculatePurchaseFee(PurchaseType type) {
        return switch (type) {
            case PHYSICAL -> BigDecimal.ZERO; // No fee
            case ONLINE -> new BigDecimal("5.00"); // $5 USD
            default -> throw new RuntimeException(type.toString());
        };
    }
}
