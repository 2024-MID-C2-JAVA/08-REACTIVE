package co.sofka;


import co.sofka.generic.Entity;
import co.sofka.values.account.AccountId;
import co.sofka.values.comun.Amount;
import co.sofka.values.account.Number;
import co.sofka.values.comun.CreatedAt;
import co.sofka.values.comun.Delete;

import java.util.List;

public class Account extends Entity<AccountId> {

    private Number number;

    private Amount amount;

    private CreatedAt createdAt;

    private Delete isDeleted;

    private List<TransactionAccountDetail> transactionAccountDetails;


    public Account(AccountId id) {
        super(id);
    }

    public Account(AccountId id, Number number, Amount amount, CreatedAt createdAt, Delete isDeleted, List<TransactionAccountDetail> transactionAccountDetails) {
        super(id);
        this.number = number;
        this.amount = amount;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
        this.transactionAccountDetails = transactionAccountDetails;
    }

    public Number getNumber() {
        return number;
    }

    public void setNumber(Number number) {
        this.number = number;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public CreatedAt getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(CreatedAt createdAt) {
        this.createdAt = createdAt;
    }

    public Delete getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Delete isDeleted) {
        this.isDeleted = isDeleted;
    }

    public List<TransactionAccountDetail> getTransactionAccountDetails() {
        return transactionAccountDetails;
    }

    public void setTransactionAccountDetails(List<TransactionAccountDetail> transactionAccountDetails) {
        this.transactionAccountDetails = transactionAccountDetails;
    }
}
