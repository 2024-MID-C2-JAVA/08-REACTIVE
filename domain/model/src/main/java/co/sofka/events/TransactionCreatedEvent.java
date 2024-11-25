package co.sofka.events;

import co.sofka.enums.TypeOfTransaction;

import java.math.BigDecimal;

public class TransactionCreatedEvent extends DomainEvent{

    private int number;
    private BigDecimal amount;
    private String customerId;
    private TypeOfTransaction type;


    public TransactionCreatedEvent() {
        super( "Transaction","Transaction created");
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }


    public TypeOfTransaction getTypeTrans() {
        return type;
    }

    public void setType(TypeOfTransaction type) {
        this.type = type;
    }
}
