package co.com.sofka.cuentaflex.libs.domain.model.accounts;

import java.util.Optional;

public interface FeeProvider {
    Fee getFeeForTransactionType(TransactionType transactionType);
}
