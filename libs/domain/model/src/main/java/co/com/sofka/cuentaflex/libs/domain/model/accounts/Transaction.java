package co.com.sofka.cuentaflex.libs.domain.model.accounts;

import co.com.sofka.cuentaflex.libs.domain.model.Entity;

import java.time.LocalDateTime;

@SuppressWarnings("ALL")
public final class Transaction extends Entity<TransactionId> {
    protected Amount amount;
    protected Fee fee;
    protected TransactionType type;
    protected LocalDateTime createdAt;

    public Transaction(TransactionId transactionId, LocalDateTime createdAt, Amount amount, Fee fee, TransactionType type) {
        super(transactionId);
        this.amount = amount;
        this.fee = fee;
        this.type = type;
        this.createdAt = createdAt;
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

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }
}
