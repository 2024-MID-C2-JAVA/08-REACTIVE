package co.sofka.commands.request;


import co.sofka.generic.Command;

import java.math.BigDecimal;

public class AccountCommand extends Command {

    private String customerId;

    private String accountId;

    private String number;

    private BigDecimal amount;

    public AccountCommand() {
    }

    public AccountCommand(String customerId, String accountId, String number, BigDecimal amount) {
        this.customerId = customerId;
        this.accountId = accountId;
        this.number = number;
        this.amount = amount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
