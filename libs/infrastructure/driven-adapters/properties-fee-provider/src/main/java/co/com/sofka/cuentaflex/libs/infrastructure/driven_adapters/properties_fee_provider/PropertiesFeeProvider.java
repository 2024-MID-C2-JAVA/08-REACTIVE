package co.com.sofka.cuentaflex.libs.infrastructure.driven_adapters.properties_fee_provider;

import co.com.sofka.cuentaflex.libs.domain.model.accounts.Fee;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.FeeProvider;
import co.com.sofka.cuentaflex.libs.domain.model.accounts.TransactionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public final class PropertiesFeeProvider implements FeeProvider {
    private final Map<TransactionType, Fee> fees;

    public PropertiesFeeProvider(
            @Value("${cuentaflex.fees.branch-deposit}") BigDecimal branchDeposit,
            @Value("${cuentaflex.fees.atm-deposit}") BigDecimal atmDeposit,
            @Value("${cuentaflex.fees.between-accounts-deposit}") BigDecimal externalAccountDeposit,
            @Value("${cuentaflex.fees.online-purchase}") BigDecimal onlinePurchase,
            @Value("${cuentaflex.fees.in-store-purchase}") BigDecimal inStorePurchase,
            @Value("${cuentaflex.fees.atm-withdrawal}") BigDecimal atmWithdrawal
    ) {
        this.fees = Map.of(
                TransactionType.DEPOSIT_FROM_BRANCH, new Fee(branchDeposit),
                TransactionType.DEPOSIT_FROM_ATM, new Fee(atmDeposit),
                TransactionType.DEPOSIT_BETWEEN_ACCOUNTS, new Fee(externalAccountDeposit),
                TransactionType.PURCHASE_ONLINE, new Fee(onlinePurchase),
                TransactionType.PURCHASE_IN_STORE, new Fee(inStorePurchase),
                TransactionType.WITHDRAW_FROM_ATM, new Fee(atmWithdrawal)
        );
    }

    @Override
    public Fee getFeeForTransactionType(TransactionType transactionType) {
        return fees.getOrDefault(transactionType, Fee.ZERO);
    }
}
