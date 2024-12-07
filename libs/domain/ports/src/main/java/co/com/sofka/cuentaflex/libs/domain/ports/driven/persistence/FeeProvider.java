package co.com.sofka.cuentaflex.libs.domain.ports.driven.persistence;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.Fee;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.TransactionType;

public interface FeeProvider {
    Fee getFeeForTransactionType(TransactionType transactionType);
}
