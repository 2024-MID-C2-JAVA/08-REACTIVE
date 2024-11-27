package co.com.sofka.cuentaflex.libs.domain.model.accounts;

import co.com.sofka.cuentaflex.libs.domain.model.BaseAuditableModel;
import co.com.sofka.cuentaflex.libs.domain.model.SoftDeletable;

import java.time.LocalDateTime;
import java.util.List;

public final class Customer extends BaseAuditableModel implements SoftDeletable {
    private String firstName;
    private String lastName;
    private String identification;
    private List<Account> accounts;
    private boolean isDeleted;

    public Customer(String id, LocalDateTime createdAt, String firstName, String lastName, String identification, List<Account> accounts, boolean isDeleted) {
        super(id, createdAt);
        this.firstName = firstName;
        this.lastName = lastName;
        this.identification = identification;
        this.accounts = accounts;
        this.isDeleted = isDeleted;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public List<Account> getAccounts() {
        return accounts.stream().toList();
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
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
