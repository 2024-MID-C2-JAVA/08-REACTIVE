package co.sofka.values.transaction;

import co.sofka.generic.Entity;
import co.sofka.generic.Identity;

public class TransactionId  extends Identity {

    public TransactionId() {
    }

    private TransactionId(String uuid) {
        super(uuid);
    }

    public static TransactionId of(String uuid){
        return new TransactionId(uuid);
    }
}
