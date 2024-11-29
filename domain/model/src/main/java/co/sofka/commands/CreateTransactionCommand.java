package co.sofka.commands;

import co.sofka.enums.TypeOfTransaction;

import java.math.BigDecimal;

public class CreateTransactionCommand {


    private BigDecimal amount;
    private TypeOfTransaction type;
    private String accountId;

    public CreateTransactionCommand() {
    }

    public CreateTransactionCommand(BigDecimal amount, TypeOfTransaction type, String accountId) {
        this.amount = amount;
        this.type = type;
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TypeOfTransaction getType() {
        return type;
    }

    public void setType(TypeOfTransaction type) {
        this.type = type;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "CreateTransactionCommand{" +
                "amount=" + amount +
                ", type=" + type +
                ", accountId='" + accountId + '\'' +
                '}';
    }
}
