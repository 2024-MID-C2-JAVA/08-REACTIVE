package com.bank.management.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Data Transfer Object for Bank Account.
 */
public class ResponseAllBankAccountByCustomerDTO {
    private final String number;
    private final String id;
    private final BigDecimal amount;

    private ResponseAllBankAccountByCustomerDTO(Builder builder) {
        this.number = builder.number;
        this.amount = builder.amount;
        this.id = builder.id;
    }

    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public static class Builder {
        private String number;
        private String id;
        private BigDecimal amount;

        public Builder number(String number) {
            this.number = number;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public ResponseAllBankAccountByCustomerDTO build() {
            return new ResponseAllBankAccountByCustomerDTO(this);
        }
    }

    @Override
    public String toString() {
        return "BankAccountDTO{" +
                "number='" + number + '\'' +
                ", amount=" + amount +
                '}';
    }
}
