package co.sofka.commands;

import co.sofka.enums.TypeOfTransaction;

import java.io.Serializable;
import java.math.BigDecimal;

public class CreateAccountCommand implements Serializable {

    private int number;
    private BigDecimal amount;
    private String customerId;
    private TypeOfTransaction type;

    public CreateAccountCommand(int number, BigDecimal amount, String customerId) {
        this.number = number;
        this.amount = amount;
        this.customerId = customerId;
    }

    public CreateAccountCommand() {
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

    public TypeOfTransaction getType() {
        return type;
    }

    public void setType(TypeOfTransaction type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CreateAccountCommand{" +
                "number=" + number +
                ", amount=" + amount +
                ", customerId='" + customerId + '\'' +
                ", type=" + type +
                '}';
    }
}
