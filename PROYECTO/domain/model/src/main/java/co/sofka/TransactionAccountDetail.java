package co.sofka;

import co.sofka.generic.Entity;
import co.sofka.values.comun.AccountTransaction;
import co.sofka.values.comun.Amount;
import co.sofka.values.comun.CreatedAt;
import co.sofka.values.comun.CustumerTransaction;
import co.sofka.values.transaction.TransactionId;
import co.sofka.values.transaction.TransactionRole;
import co.sofka.values.transaction.TypeTransaction;

public class TransactionAccountDetail extends Entity<TransactionId> {



    private CustumerTransaction custumerTransactionSenderID;

    private CustumerTransaction custumerTransactionResiverId;

    private AccountTransaction numberAccountTransactionSenderID;

    private AccountTransaction numberAccountTransactionResiverId;

    private Amount amountTransaction;

    private Amount transactionCost;

    private TypeTransaction typeTransaction;

    private CreatedAt timeStamp;

    private TransactionRole transactionRole;

    public TransactionAccountDetail(TransactionId id) {
        super(id);
    }

    public TransactionAccountDetail(TransactionId id, CustumerTransaction custumerTransactionSenderID, CustumerTransaction custumerTransactionResiverId, AccountTransaction numberAccountTransactionSenderID, AccountTransaction numberAccountTransactionResiverId, Amount amountTransaction, Amount transactionCost, TypeTransaction typeTransaction, CreatedAt timeStamp, TransactionRole transactionRole) {
        super(id);
        this.custumerTransactionSenderID = custumerTransactionSenderID;
        this.custumerTransactionResiverId = custumerTransactionResiverId;
        this.numberAccountTransactionSenderID = numberAccountTransactionSenderID;
        this.numberAccountTransactionResiverId = numberAccountTransactionResiverId;
        this.amountTransaction = amountTransaction;
        this.transactionCost = transactionCost;
        this.typeTransaction = typeTransaction;
        this.timeStamp = timeStamp;
        this.transactionRole = transactionRole;
    }

    public CustumerTransaction getCustumerTransactionSenderID() {
        return custumerTransactionSenderID;
    }

    public void setCustumerTransactionSenderID(CustumerTransaction custumerTransactionSenderID) {
        this.custumerTransactionSenderID = custumerTransactionSenderID;
    }

    public CustumerTransaction getCustumerTransactionResiverId() {
        return custumerTransactionResiverId;
    }

    public void setCustumerTransactionResiverId(CustumerTransaction custumerTransactionResiverId) {
        this.custumerTransactionResiverId = custumerTransactionResiverId;
    }

    public AccountTransaction getNumberAccountTransactionSenderID() {
        return numberAccountTransactionSenderID;
    }

    public void setNumberAccountTransactionSenderID(AccountTransaction numberAccountTransactionSenderID) {
        this.numberAccountTransactionSenderID = numberAccountTransactionSenderID;
    }

    public AccountTransaction getNumberAccountTransactionResiverId() {
        return numberAccountTransactionResiverId;
    }

    public void setNumberAccountTransactionResiverId(AccountTransaction numberAccountTransactionResiverId) {
        this.numberAccountTransactionResiverId = numberAccountTransactionResiverId;
    }

    public Amount getAmountTransaction() {
        return amountTransaction;
    }

    public void setAmountTransaction(Amount amountTransaction) {
        this.amountTransaction = amountTransaction;
    }

    public Amount getTransactionCost() {
        return transactionCost;
    }

    public void setTransactionCost(Amount transactionCost) {
        this.transactionCost = transactionCost;
    }

    public TypeTransaction getTypeTransaction() {
        return typeTransaction;
    }

    public void setTypeTransaction(TypeTransaction typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public CreatedAt getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(CreatedAt timeStamp) {
        this.timeStamp = timeStamp;
    }

    public TransactionRole getTransactionRole() {
        return transactionRole;
    }

    public void setTransactionRole(TransactionRole transactionRole) {
        this.transactionRole = transactionRole;
    }
}
