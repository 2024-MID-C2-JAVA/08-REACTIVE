package co.com.sofka.cuentaflex.libs.domain.model.accounts.events;

import co.com.sofka.cuentaflex.libs.domain.model.DomainEvent;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.Customer;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.TransactionType;

import java.math.BigDecimal;

public final class UnidirectionalTransactionPerformedEvent extends DomainEvent {
    private String accountId;
    private String transactionId;
    private BigDecimal amount;
    private BigDecimal fee;
    private TransactionType transactionType;

    public UnidirectionalTransactionPerformedEvent() {
        super(
                UnidirectionalTransactionPerformedEvent.class.getName(),
                Customer.class.getName()
        );
    }

    public UnidirectionalTransactionPerformedEvent(String customerId, String accountId, String transactionId, BigDecimal amount, BigDecimal fee, TransactionType transactionType) {
        super(
                UnidirectionalTransactionPerformedEvent.class.getName(),
                customerId,
                Customer.class.getName()
        );

        this.accountId = accountId;
        this.transactionId = transactionId;
        this.amount = amount;
        this.fee = fee;
        this.transactionType = transactionType;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
