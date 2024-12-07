package co.com.sofka.cuentaflex.libs.domain.use_cases.exceptions;

public final class AccountDoesNotExistsException extends RuntimeException {
    public AccountDoesNotExistsException(String accountId) {
        super("The account with identification '" + accountId + "' does not exists");
    }
}
