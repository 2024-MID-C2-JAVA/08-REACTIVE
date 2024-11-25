package co.sofka.dto;

import java.math.BigDecimal;

public class BankTransactionDepositTransfer {

    private String customerSenderId;
    private String accountNumberSender;

    private String customerReceiverId;
    private String accountNumberReceiver;

    private BigDecimal amount;


    public BankTransactionDepositTransfer() {
    }

    public String getCustomerSenderId() {
        return customerSenderId;
    }

    public void setCustomerSenderId(String customerSenderId) {
        this.customerSenderId = customerSenderId;
    }

    public String getCustomerReceiverId() {
        return customerReceiverId;
    }

    public void setCustomerReceiverId(String customerReceiverId) {
        this.customerReceiverId = customerReceiverId;
    }

    public String getAccountNumberSender() {
        return accountNumberSender;
    }

    public void setAccountNumberSender(String accountNumberSender) {
        this.accountNumberSender = accountNumberSender;
    }

    public String getAccountNumberReceiver() {
        return accountNumberReceiver;
    }

    public void setAccountNumberReceiver(String accountNumberReceiver) {
        this.accountNumberReceiver = accountNumberReceiver;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


}
