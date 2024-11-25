package com.bank.management.mapper;

import com.bank.management.transaction.Transaction;
import com.bank.management.data.TransactionDocument;

public class TransactionMapper {

    public static TransactionDocument toDocument(Transaction transaction) {
        return new TransactionDocument(
                transaction.getId(),
                transaction.getAmountTransaction(),
                transaction.getTransactionCost(),
                transaction.getTypeTransaction(),
                transaction.getTimeStamp()
        );
    }

    public static Transaction toDomain(TransactionDocument document) {
        return new Transaction.Builder()
                .id(document.getId())
                .amountTransaction(document.getAmountTransaction())
                .transactionCost(document.getTransactionCost())
                .typeTransaction(document.getTypeTransaction())
                .timeStamp(document.getTimeStamp())
                .build();
    }
}
