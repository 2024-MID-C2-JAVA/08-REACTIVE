package co.sofka.appservice.transaction.strategy;

import co.sofka.Transaction;

import java.math.BigDecimal;


public class Withdraw implements TypeTransaction {
    @Override
    public Transaction movement(Transaction transaction) {

        BigDecimal cost = new BigDecimal("1");

        BigDecimal newAmount = transaction.getAmount().subtract(cost);

        transaction.setAmount(newAmount);
        transaction.setAmountCost(cost);

        return transaction;
    }
}
