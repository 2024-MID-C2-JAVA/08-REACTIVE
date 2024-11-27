package co.com.sofka.cuentaflex.libs.domain.model.accounts;

import co.com.sofka.cuentaflex.libs.domain.model.BaseAuditableModel;

import java.time.LocalDateTime;

public final class Transaction extends BaseAuditableModel {
    private Amount amount;
    private Fee fee;
    private TransactionType type;

    public Transaction(String id, LocalDateTime createdAt, Amount amount, Fee fee, TransactionType type) {
        super(id, createdAt);
        this.amount = amount;
        this.fee = fee;
        this.type = type;
    }

    public Amount getAmount() {
        return this.amount;
    }

    public Fee getFee() {
        return this.fee;
    }

    public TransactionType getType() {
        return this.type;
    }
}
