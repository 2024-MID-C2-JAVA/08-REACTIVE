package co.com.sofka.cuentaflex.libs.domain.model.accounts;

import co.com.sofka.cuentaflex.libs.domain.model.AggregateRoot;
import co.com.sofka.cuentaflex.libs.domain.model.DomainEvent;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.events.*;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("ALL")
public final class Customer extends AggregateRoot<CustomerId> {
    protected String firstName;
    protected String lastName;
    protected String identification;
    protected List<Account> accounts;
    protected LocalDateTime createdAt;
    protected boolean isDeleted;

    private Customer(CustomerId customerId) {
        super(customerId);
        this.subscribe(new CustomerChange(this));
    }

    public Customer(CustomerId customerId, String firstName, String lastName, String identification) {
        super(customerId);
        this.subscribe(new CustomerChange(this));
        this.appendChange(new CustomerCreatedEvent(
                customerId.getValue(),
                firstName,
                lastName,
                identification
        ));
    }

    public static Customer from(CustomerId customerId, List<DomainEvent> events) {
        Customer customer = new Customer(customerId);
        events.forEach(customer::applyEvent);
        return customer;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getIdentification() {
        return identification;
    }

    public List<Account> getAccounts() {
        return List.copyOf(accounts);
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
        this.appendChange(new AccountCreatedEvent(
                account.getId().getValue(),
                this.getId().getValue(),
                account.getAccountNumber(),
                account.getBalance().getValue()
        ));
    }

    public void addTransactionToAccount(AccountId accountId, Transaction transaction) {
        if (!transaction.getType().isUnidirectional()) {
            throw new IllegalArgumentException("The transaction type must be unidirectional");
        }

        this.accounts.stream()
                .filter(account -> account.getId().equals(accountId))
                .findFirst()
                .ifPresent(account -> {
                    account.addTransaction(transaction);
                    this.appendChange(new UnidirectionalTransactionPerformedEvent(
                            this.getId().getValue(),
                            accountId.getValue(),
                            transaction.getId().getValue(),
                            transaction.amount.getValue(),
                            transaction.fee.getValue(),
                            transaction.getType()
                    ));
                });
    }

    public void addDebitTransactionBetweenAccounts(AccountId accountId, Transaction transaction) {
        if(transaction.getType() != TransactionType.DEPOSIT_BETWEEN_ACCOUNTS) {
            throw new IllegalArgumentException("The transaction type must be DEPOSIT_BETWEEN_ACCOUNTS");
        }

        this.accounts.stream()
                .filter(account -> account.getId().equals(accountId))
                .findFirst()
                .ifPresent(account -> {
                    account.addTransaction(transaction);
                    this.appendChange(new FundsDebitedInDepositBetweenAccountsEvent(
                            this.getId().getValue(),
                            accountId.getValue(),
                            transaction.getId().getValue(),
                            transaction.amount.getValue(),
                            transaction.fee.getValue()
                    ));
                });
    }

    public void addCreditTransactionBetweenAccounts(AccountId accountId, Transaction transaction) {
        if(transaction.getType() != TransactionType.DEPOSIT_BETWEEN_ACCOUNTS) {
            throw new IllegalArgumentException("The transaction type must be DEPOSIT_BETWEEN_ACCOUNTS");
        }

        this.accounts.stream()
                .filter(account -> account.getId().equals(accountId))
                .findFirst()
                .ifPresent(account -> {
                    account.addTransaction(transaction);
                    this.appendChange(new FundsCreditedInDepositBetweenAccountsEvent(
                            this.getId().getValue(),
                            accountId.getValue(),
                            transaction.getId().getValue(),
                            transaction.amount.getValue(),
                            transaction.fee.getValue()
                    ));
                });
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }
}
