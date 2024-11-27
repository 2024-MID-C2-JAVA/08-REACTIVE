package co.com.sofka.cuentaflex.libs.domain.model.accounts;

import co.com.sofka.cuentaflex.libs.domain.model.BaseAuditableModel;
import co.com.sofka.cuentaflex.libs.domain.model.SoftDeletable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public final class Account extends BaseAuditableModel implements SoftDeletable {
    private int accountNumber;
    private Balance balance;
    private final List<Transaction> transactions;
    private boolean isDeleted;

    public Account(String id, LocalDateTime createdAt, int accountNumber, Balance balance, List<Transaction> transactions, boolean isDeleted) {
        super(id, createdAt);
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.transactions = transactions;
        this.isDeleted = isDeleted;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public void debit(Amount amount) {
        Objects.requireNonNull(amount, "The amount to debit cannot be null");

        if (amount.getValue().compareTo(this.balance.getValue()) > 0) {
            throw new InsufficientFundsExceptions(this.getId(), amount, this.balance);
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

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public List<Transaction> getTransactions() {
        return this.transactions.stream().toList();
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    @Override
    public boolean isDeleted() {
        return this.isDeleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }
}
