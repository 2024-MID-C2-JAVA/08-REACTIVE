package co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence.data;

public class AccountTransactionView {
    private TransactionView transaction;
    private AccountRole accountRole;

    public AccountTransactionView() {
    }

    public AccountTransactionView( TransactionView transaction, AccountRole accountRole) {
        this.transaction = transaction;
        this.accountRole = accountRole;
    }

    public TransactionView getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionView transaction) {
        this.transaction = transaction;
    }

    public AccountRole getAccountRole() {
        return accountRole;
    }

    public void setAccountRole(AccountRole accountRole) {
        this.accountRole = accountRole;
    }
}
