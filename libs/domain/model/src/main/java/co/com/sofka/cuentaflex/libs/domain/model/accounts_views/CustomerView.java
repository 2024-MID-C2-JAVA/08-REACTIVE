package co.com.sofka.cuentaflex.libs.domain.model.accounts_views;

import java.time.LocalDateTime;
import java.util.List;

public class CustomerView {
    private String customerId;
    private String firstName;
    private String lastName;
    private String identification;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private List<AccountView> accounts;

    public CustomerView() {
    }

    public CustomerView(String customerId, String firstName, String lastName, String identification, boolean isDeleted, LocalDateTime createdAt, List<AccountView> accounts) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.identification = identification;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.accounts = accounts;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public List<AccountView> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountView> accounts) {
        this.accounts = accounts;
    }
}
