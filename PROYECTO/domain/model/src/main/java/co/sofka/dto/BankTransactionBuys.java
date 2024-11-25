package co.sofka.dto;


import java.math.BigDecimal;

public class BankTransactionBuys extends ParameterSeguridad{

    private String customerId;

    private String accountNumberClient;

    private BigDecimal amount;


    private int typeBuys;


    public BankTransactionBuys() {
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAccountNumberClient() {
        return accountNumberClient;
    }

    public void setAccountNumberClient(String accountNumberClient) {
        this.accountNumberClient = accountNumberClient;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getTypeBuys() {
        return typeBuys;
    }

    public void setTypeBuys(int typeBuys) {
        this.typeBuys = typeBuys;
    }


}
