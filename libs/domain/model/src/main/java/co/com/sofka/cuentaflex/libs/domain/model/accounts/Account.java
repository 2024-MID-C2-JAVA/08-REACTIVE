package co.com.sofka.cuentaflex.libs.domain.model.accounts;

import co.com.sofka.cuentaflex.libs.domain.model.DomainEvent;
import co.com.sofka.cuentaflex.libs.domain.model.Entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("ALL")
public final class Account extends Entity<AccountId> {
    protected int accountNumber;
    protected Balance balance;
    protected List<Transaction> transactions;
    protected boolean isDeleted;
    protected LocalDateTime createdAt;

    public Account(AccountId id, int accountNumber, Balance balance) {
        super(id);
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public Balance getBalance() {
        return balance;
    }

    public void debit(Amount amount) {
        Objects.requireNonNull(amount, "The amount to debit cannot be null");

        if (amount.getValue().compareTo(this.balance.getValue()) > 0) {
            throw new InsufficientFundsExceptions(this.getId().getValue(), amount, this.balance);
        }
        this.balance = this.balance.subtract(amount);
    }

    public void credit(Amount amount) {
        Objects.requireNonNull(amount, "The amount to credit cannot be null");
        this.balance = this.balance.add(amount);
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public List<Transaction> getTransactions() {
        return List.copyOf(this.transactions);
    }

    protected void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }
}
