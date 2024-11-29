package com.bank.management.usecase.queryservice;

import com.bank.management.command.ProcessWithdrawalCommand;
import com.bank.management.values.Account;
import com.bank.management.values.Customer;
import com.bank.management.exception.BankAccountNotFoundException;
import com.bank.management.exception.CustomerNotFoundException;
import com.bank.management.exception.InsufficientFundsException;
import com.bank.management.exception.InvalidAmountException;
import com.bank.management.gateway.AccountRepository;
import com.bank.management.gateway.CustomerRepository;
import com.bank.management.gateway.TransactionRepository;
import com.bank.management.values.Transaction;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class UpdateViewWithdrawAddedUseCase {


    private final AccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    public UpdateViewWithdrawAddedUseCase(AccountRepository bankAccountRepository, CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    public Mono<Account> apply(ProcessWithdrawalCommand command) {
        return validateWithdrawalAmount(command.getAmount())
                .then(bankAccountRepository.findByNumber(command.getAccountNumber())
                        .switchIfEmpty(Mono.error(new BankAccountNotFoundException())))
                .zipWith(customerRepository.findByUsername(command.getUsername())
                        .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found"))))
                .flatMap(tuple -> {
                    Account account = tuple.getT1();
                    Customer customer = tuple.getT2();

                    BigDecimal transactionFee = new BigDecimal("1.00");
                    BigDecimal totalCharge = command.getAmount().add(transactionFee);

                    if (account.getAmount().value().compareTo(totalCharge) < 0) {
                        return Mono.error(new InsufficientFundsException());
                    }

                    account.adjustBalance(totalCharge.negate());

                    Transaction trx = new Transaction.Builder()
                            .amountTransaction(command.getAmount())
                            .transactionCost(transactionFee)
                            .typeTransaction("WITHDRAWAL")
                            .build();

                    return transactionRepository.save(trx, account, customer, "RECEIVED")
                            .then(Mono.just(account));
                });
    }

    private Mono<Void> validateWithdrawalAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.error(new InvalidAmountException());
        }
        return Mono.empty();
    }


}
