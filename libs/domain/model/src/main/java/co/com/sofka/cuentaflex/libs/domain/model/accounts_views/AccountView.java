package co.com.sofka.cuentaflex.libs.domain.model.accounts_views;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class AccountView {
    private String accountId;
    private int accountNumber;
    private BigDecimal balance;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private List<AccountTransactionView> transactions;

    public AccountView() {}

    public AccountView(String accountId, int accountNumber, BigDecimal balance, boolean isDeleted, LocalDateTime createdAt, List<AccountTransactionView> transactions) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.transactions = transactions;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<AccountTransactionView> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<AccountTransactionView> transactions) {
        this.transactions = transactions;
    }
}
