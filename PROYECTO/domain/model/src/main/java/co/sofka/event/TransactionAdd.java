package co.sofka.event;

import co.sofka.generic.DomainEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionAdd extends DomainEvent {


    private String transactionId;

    private String custumerTransactionSenderID;

    private String custumerTransactionResiverId;

    private String numberAccountTransactionSenderID;

    private String numberAccountTransactionResiverId;

    private BigDecimal amountTransaction;

    private BigDecimal transactionCost;

    private String typeTransaction;

    private LocalDateTime timeStamp;

    private String transactionRole;


    public TransactionAdd() {
        super("CreateTransaction");
    }

    public TransactionAdd(String transactionId, String custumerTransactionSenderID, String custumerTransactionResiverId, String numberAccountTransactionSenderID, String numberAccountTransactionResiverId, BigDecimal amountTransaction, BigDecimal transactionCost, String typeTransaction, LocalDateTime timeStamp, String transactionRole) {
        super("CreateTransaction");
        this.transactionId = transactionId;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCustumerTransactionSenderID() {
        return custumerTransactionSenderID;
    }

    public void setCustumerTransactionSenderID(String custumerTransactionSenderID) {
        this.custumerTransactionSenderID = custumerTransactionSenderID;
    }

    public String getCustumerTransactionResiverId() {
        return custumerTransactionResiverId;
    }

    public void setCustumerTransactionResiverId(String custumerTransactionResiverId) {
        this.custumerTransactionResiverId = custumerTransactionResiverId;
    }

    public String getNumberAccountTransactionSenderID() {
        return numberAccountTransactionSenderID;
    }

    public void setNumberAccountTransactionSenderID(String numberAccountTransactionSenderID) {
        this.numberAccountTransactionSenderID = numberAccountTransactionSenderID;
    }

    public String getNumberAccountTransactionResiverId() {
        return numberAccountTransactionResiverId;
    }

    public void setNumberAccountTransactionResiverId(String numberAccountTransactionResiverId) {
        this.numberAccountTransactionResiverId = numberAccountTransactionResiverId;
    }

    public BigDecimal getAmountTransaction() {
        return amountTransaction;
    }

    public void setAmountTransaction(BigDecimal amountTransaction) {
        this.amountTransaction = amountTransaction;
    }

    public BigDecimal getTransactionCost() {
        return transactionCost;
    }

    public void setTransactionCost(BigDecimal transactionCost) {
        this.transactionCost = transactionCost;
    }

    public String getTypeTransaction() {
        return typeTransaction;
    }

    public void setTypeTransaction(String typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTransactionRole() {
        return transactionRole;
    }

    public void setTransactionRole(String transactionRole) {
        this.transactionRole = transactionRole;
    }
}
