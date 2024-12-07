package co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_webservice.data;

import java.math.BigDecimal;

public class PerformUnidirectionalTransactionRequest {
    private String customerId;
    private String accountId;
    private String transactionId;
    private BigDecimal amount;

    public PerformUnidirectionalTransactionRequest(String customerId, String accountId, String transactionId, BigDecimal amount) {
        this.customerId = customerId;
        this.accountId = accountId;
        this.transactionId = transactionId;
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
}
