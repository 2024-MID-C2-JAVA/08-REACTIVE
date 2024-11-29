package com.bank.management.values.transaction;

import com.bank.management.generic.Identity;

public class TransactionId extends Identity {

    public TransactionId() {
    }

    private TransactionId(String uuid) {
        super(uuid);
    }


    public static TransactionId of(String uuid){
        return new TransactionId(uuid);
    }
}
