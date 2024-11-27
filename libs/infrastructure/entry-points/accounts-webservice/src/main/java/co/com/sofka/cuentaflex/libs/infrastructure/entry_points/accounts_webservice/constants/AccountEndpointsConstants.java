package co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_webservice.constants;

public final class AccountEndpointsConstants {
    public static final String CUSTOMER_ACCOUNTS_BASE_URL = "/customers";

    public static final String BRANCH_DEPOSIT_TO_ACCOUNT_URL = CUSTOMER_ACCOUNTS_BASE_URL + "/PerformBranchDeposit";
    public static final String ATM_DEPOSIT_TO_ACCOUNT_URL = CUSTOMER_ACCOUNTS_BASE_URL + "/PerformAtmDeposit";
    public static final String DEPOSIT_BETWEEN_ACCOUNTS_URL = CUSTOMER_ACCOUNTS_BASE_URL + "/PerformDepositBetweenAccounts";

    public static final String ONLINE_PURCHASE_TO_ACCOUNT_URL = CUSTOMER_ACCOUNTS_BASE_URL + "/PerformOnlinePurchase";
    public static final String IN_STORE_PURCHASE_TO_ACCOUNT_URL = CUSTOMER_ACCOUNTS_BASE_URL + "/PerformInStorePurchase";

    public static final String ATM_WITHDRAWAL_TO_ACCOUNT_URL = CUSTOMER_ACCOUNTS_BASE_URL + "/PerformAtmWithdrawal";
}
