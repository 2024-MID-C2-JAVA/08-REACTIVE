package co.com.sofka.cuentaflex.libs.infrastructure.entry_points.accounts_webservice.din_errors;

import co.com.sofka.cuentaflex.libs.infrastructure.utils.din.DinError;
import co.com.sofka.cuentaflex.libs.infrastructure.utils.din.DinErrorType;

public final class DinErrors {
    public static DinError withDetail(final DinError error, final String detail) {
        return new DinError(
                error.getType(),
                error.getSource(),
                error.getCode(),
                error.getProviderErrorCode(),
                error.getMessage(),
                detail
        );
    }


    public static DinError CUSTOMER_ALREADY_EXISTS = new DinError(
            DinErrorType.ERROR,
            "Database",
            "1007",
            null,
            "Customer already exists",
            ""
    );


}
