package co.sofka;

import co.sofka.event.AccountAdd;
import co.sofka.event.CustomerCreated;
import co.sofka.event.TransactionAdd;
import co.sofka.generic.AggregateRoot;
import co.sofka.generic.DomainEvent;
import co.sofka.values.account.AccountId;
import co.sofka.values.account.Number;
import co.sofka.values.comun.AccountTransaction;
import co.sofka.values.comun.Amount;
import co.sofka.values.comun.CustumerTransaction;
import co.sofka.values.customer.CustomerId;
import co.sofka.values.customer.Password;
import co.sofka.values.customer.Rol;
import co.sofka.values.customer.UserName;
import co.sofka.values.transaction.TransactionId;
import co.sofka.values.transaction.TransactionRole;
import co.sofka.values.transaction.TypeTransaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Customer extends AggregateRoot<CustomerId> {


    protected UserName username;

    protected Password pwd;

    protected Rol rol;

    protected List<Account> accounts;


    public Customer(CustomerId id) {
        super(id);
        subscribe(new CustomerChange(this));
    }

    public Customer(CustomerId id, UserName username, Password pwd, Rol rol) {
        super(id);
        this.username = username;
        this.pwd = pwd;
        this.rol = rol;
        subscribe(new CustomerChange(this));
        appendChange(new CustomerCreated(username.value(), pwd.value(), rol.value())).apply();
    }

    public static Customer from(CustomerId customerId, List<DomainEvent> events) {
        Customer customer = new Customer(customerId);
        events.forEach(customer::applyEvent);
        return customer;
    }

    public void addAccount(AccountId accountId, Number number, Amount amount) {
        Objects.requireNonNull(accountId, "Entity id cannot be null");
        Objects.requireNonNull(number, "Numero cannot be null");
        Objects.requireNonNull(amount, "Number cannot be null");
        appendChange(new AccountAdd(accountId.value(), number.value(), amount.value(), LocalDateTime.now(), false));
    }

    public void addAccountTransaction(TransactionId transactionId, CustumerTransaction custumerTransactionSenderID, CustumerTransaction custumerTransactionResiverId,
                                      AccountTransaction numberAccountTransactionSenderID, AccountTransaction numberAccountTransactionResiverId,
                                      Amount amountTransaction, Amount transactionCost, TypeTransaction typeTransaction, TransactionRole transactionRole) {
        Objects.requireNonNull(transactionId, "Entity id cannot be null");
        Objects.requireNonNull(custumerTransactionSenderID, "Customer Sender cannot be null");
        Objects.requireNonNull(custumerTransactionResiverId, "Customer Resiver cannot be null");
        Objects.requireNonNull(numberAccountTransactionSenderID, "Account Number Sender cannot be null");
        Objects.requireNonNull(numberAccountTransactionResiverId, "Account Number Resiver cannot be null");
        Objects.requireNonNull(amountTransaction, "Amount cannot be null");
        Objects.requireNonNull(transactionCost, "Transaction Cost cannot be null");
        Objects.requireNonNull(typeTransaction, "Type Transaction cannot be null");
        Objects.requireNonNull(transactionRole, "Transaction Role cannot be null");
        appendChange(new TransactionAdd(transactionId.value(), custumerTransactionSenderID.value(), custumerTransactionResiverId.value(),
                numberAccountTransactionSenderID.value(), numberAccountTransactionResiverId.value(), amountTransaction.value(), transactionCost.value(),
                typeTransaction.value(), LocalDateTime.now(), transactionRole.value()));
    }


}
